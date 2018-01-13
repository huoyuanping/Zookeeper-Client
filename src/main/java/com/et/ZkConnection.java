package com.et;

import java.sql.Connection;
import java.sql.DriverManager;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;


public class ZkConnection {
	public static Connection conn=null;
	/**
	 * 获取连接
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
		//10000超时时间  connectionTimeout：50000自动连接不到五秒后自动连接
		//添加新的序列化对象
		final ZkClient zk=new ZkClient(zkUrl,10000,5000,new BytesPushThroughSerializer());
		byte[] url=zk.readData("/db/url");
		byte[] driverClass=zk.readData("/db/driverClass");
		byte[] username=zk.readData("/db/username");
		byte[] password=zk.readData("/db/password");
		try {
			Connection conn=getConnection(new String(driverClass),new String(url), new String (username), new String (password));
			System.out.println(conn);
			//监听

			//默认会结束  匿名类不可以直接访问父类中直接定义的变量要加final
			zk.subscribeDataChanges("/db/url", new IZkDataListener() {
				
				//db节点删除时触发
				public void handleDataDeleted(String path) throws Exception {
					System.out.println(path);
				}
				
				//db节点修改时触发  path:路径  data值
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
