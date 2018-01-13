package com.et;

import org.I0Itec.zkclient.ZkClient;

public class MySqlTest {
	public static void main(String[] args) {
		String zkUrl="localhost:2181";
		//10000超时时间  connectionTimeout：50000自动连接不到五秒后自动连接
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		

		zk.writeData("/db/url","jdbc\\:oracle\\:thin\\:@localhost\\:1521\\:orcl");
		zk.writeData("/db/driverClass","oracle.jdbc.driver.OracleDriver");
		zk.writeData("/db/username","root");
		zk.writeData("/db/password","123456");
	}
}
