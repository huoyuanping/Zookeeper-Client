package com.et;

import org.I0Itec.zkclient.ZkClient;

public class Test {
	public static void main(String[] args) {
		String zkUrl="localhost:2181";
		//10000��ʱʱ��  connectionTimeout��50000�Զ����Ӳ���������Զ�����
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		zk.writeData("/db","mysql");
	}
}
