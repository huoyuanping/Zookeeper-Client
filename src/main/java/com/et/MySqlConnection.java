package com.et;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class MySqlConnection {
	public static void main(String[] args) {
		String zkUrl="localhost:2181";
		//10000超时时间  connectionTimeout：50000自动连接不到五秒后自动连接
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		
		if(!zk.exists("/db")){
			//创建一个永久节点/user
			zk.createPersistent("/db","mysql");
			//创建四要素
			zk.createPersistent("/db/url", "jdbc:mysql://localhost:3306/food");
			zk.createPersistent("/db/driverClass", "com.mysql.jdbc.Driver");
			zk.createPersistent("/db/username", "root");
			zk.createPersistent("/db/password", "123456");
		}
		String url=zk.readData("/db/url");
		String driverClass=zk.readData("/db/driverClass");
		String username=zk.readData("/db/username");
		String password=zk.readData("/db/password");
		System.out.println("url为："+url);
		try {
			Connection con=DriverManager.getConnection(url,username,password);
			Class.forName(driverClass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//匿名类不可直接访问父类中的变量要加final
		
		//默认会结束
		zk.subscribeDataChanges("/db/driverClass", new IZkDataListener() {
			
			//db节点删除时触发
			public void handleDataDeleted(String path) throws Exception {
				System.out.println(path);
			}
			
			//db节点修改时触发  path:路径  data值
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println(path);
				System.out.println(data);
			}
		});
		
		//死循环不会结束
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
