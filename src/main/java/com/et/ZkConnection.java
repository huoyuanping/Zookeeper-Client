package com.et;

import java.sql.Connection;
import java.sql.DriverManager;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;


public class ZkConnection {
	public static Connection conn=null;
	/**
	 * ��ȡ����
	 * @return
	 */
	public static Connection getConnection(String driverClass,String url,String username,String password){
		
		Connection conn=null;
		try {
			Class.forName(new String(driverClass));
			conn = DriverManager.getConnection(new String(url), new String (username), new String (password));
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	
		return conn;
	}
	public static void main(String[] args) {
		String zkUrl="localhost:2181";
		//10000��ʱʱ��  connectionTimeout��50000�Զ����Ӳ���������Զ�����
		//����µ����л�����
		final ZkClient zk=new ZkClient(zkUrl,10000,5000,new BytesPushThroughSerializer());
		byte[] url=zk.readData("/db/url");
		byte[] driverClass=zk.readData("/db/driverClass");
		byte[] username=zk.readData("/db/username");
		byte[] password=zk.readData("/db/password");
		try {
			Connection conn=getConnection(new String(driverClass),new String(url), new String (username), new String (password));
			System.out.println(conn);
			//����

			//Ĭ�ϻ����  �����಻����ֱ�ӷ��ʸ�����ֱ�Ӷ���ı���Ҫ��final
			zk.subscribeDataChanges("/db/url", new IZkDataListener() {
				
				//db�ڵ�ɾ��ʱ����
				public void handleDataDeleted(String path) throws Exception {
					System.out.println(path);
				}
				
				//db�ڵ��޸�ʱ����  path:·��  dataֵ
				public void handleDataChange(String path, Object data) throws Exception {
					byte[] url=zk.readData("/db/url");
					byte[] driverClass=zk.readData("/db/driverClass");
					byte[] username=zk.readData("/db/username");
					byte[] password=zk.readData("/db/password");
					Connection conn=getConnection(new String(driverClass),new String(url), new String (username), new String (password));
					System.out.println(conn);
				}
			});
			
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
