package com.crm.provisioning.cache;

import javax.naming.NamingException;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.crm.kernel.queue.QueueFactory;

public class MQConnectionPool extends ObjectPool
{
	private int					maxActive	= 10;

	protected static Logger	log	= Logger.getLogger(MQConnectionPool.class);
	
	public MQConnectionPool(int maxActive)
	{
		this.maxActive = maxActive;
	}

	public MQConnectionPool(int maxActive, int maxWait, int maxIdle)
	{
		this.maxActive = maxActive;
	}

	@Override
	public void debugMonitor(Object message)
	{
	}

	@Override
	public GenericObjectPool initPool() throws Exception
	{
		/**
		 * This pool must be FIFO (LIFO = false) be cause in case of connection
		 * has consumer, all connection must be get to read in sequence to
		 * detach message from consumer batch.
		 */
		GenericObjectPool objectPool =
				new GenericObjectPool(
						this, maxActive, GenericObjectPool.WHEN_EXHAUSTED_BLOCK, QueueFactory.queuePoolWaitTime
						, maxActive, maxActive, true, true, 0, 0, 0, false, 0, false);

		return objectPool;
	}

	@Override
	protected Object createObject() throws Exception
	{
		try
		{
			return new MQConnection();
		}
		catch (NamingException ne)
		{
			QueueFactory.resetContext();

			throw ne;
		}
	}

	public MQConnection getConnection() throws Exception
	{
		return (MQConnection) getObject();
	}

	public void returnConnection(MQConnection connection)
	{
		if (connection == null)
		{
			return;
		}

		returnObject(connection);
	}
}
