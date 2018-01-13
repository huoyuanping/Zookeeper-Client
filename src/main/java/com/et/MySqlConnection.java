package com.et;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class MySqlConnection {
	public static void main(String[] args) {
		String zkUrl="localhost:2181";
		//10000��ʱʱ��  connectionTimeout��50000�Զ����Ӳ���������Զ�����
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		
		if(!zk.exists("/db")){
			//����һ�����ýڵ�/user
			zk.createPersistent("/db","mysql");
			//������Ҫ��
			zk.createPersistent("/db/url", "jdbc:mysql://localhost:3306/food");
			zk.createPersistent("/db/driverClass", "com.mysql.jdbc.Driver");
			zk.createPersistent("/db/username", "root");
			zk.createPersistent("/db/password", "123456");
		}
		String url=zk.readData("/db/url");
		String driverClass=zk.readData("/db/driverClass");
		String username=zk.readData("/db/username");
		String password=zk.readData("/db/password");
		System.out.println("urlΪ��"+url);
		try {
			Connection con=DriverManager.getConnection(url,username,password);
			Class.forName(driverClass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//�����಻��ֱ�ӷ��ʸ����еı���Ҫ��final
		
		//Ĭ�ϻ����
		zk.subscribeDataChanges("/db/driverClass", new IZkDataListener() {
			
			//db�ڵ�ɾ��ʱ����
			public void handleDataDeleted(String path) throws Exception {
				System.out.println(path);
			}
			
			//db�ڵ��޸�ʱ����  path:·��  dataֵ
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println(path);
				System.out.println(data);
			}
		});
		
		//��ѭ���������
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
