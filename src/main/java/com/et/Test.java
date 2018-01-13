package com.et;

import org.I0Itec.zkclient.ZkClient;

public class Test {
	public static void main(String[] args) {
		String zkUrl="localhost:2181";
		//10000超时时间  connectionTimeout：50000自动连接不到五秒后自动连接
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		zk.writeData("/db","mysql");
	}
}
