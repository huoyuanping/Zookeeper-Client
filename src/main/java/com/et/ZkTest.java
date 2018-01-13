package com.et;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZkTest {
	public static void main(String[] args) throws InterruptedException {
		//����zookeeperһ̨�ﲻ����д�����������ö��Ÿ���
		String zkUrl="localhost:2181";
		//10000��ʱʱ��  connectionTimeout��50000�Զ����Ӳ���������Զ�����
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		if(!zk.exists("/user")){
			//����һ�����ýڵ�/user
			zk.createPersistent("/user");
			//��������˳��ڵ�   EPHEMERAL_SEQUENTIAL��ʱ��˳��ڵ�  ���õ�˳��ڵ�PERSISTENT_SEQUENTIAL
			//����˳��ڵ���ʵ������
			String znodeName=zk.create("/user/ls","boy", CreateMode.PERSISTENT_SEQUENTIAL);
			String znodeName1=zk.create("/user/ls","boy", CreateMode.PERSISTENT_SEQUENTIAL);
			System.out.println(znodeName);
			System.out.println(znodeName1);
			//����һ����ʱ�ڵ�
			zk.createEphemeral("/user/zs", "girl");
			//�����߳����߾Ͳ����˳�
			//Thread.sleep(Integer.MAX_VALUE);
		}
		//Ĭ�ϻ����
		zk.subscribeDataChanges("/db", new IZkDataListener() {
			
			//db�ڵ�ɾ��ʱ����
			public void handleDataDeleted(String path) throws Exception {
				System.out.println(path);

			}
			
			//db�ڵ��޸�ʱ����  path:·��  dataֵ
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println(path);
			}
		});
		
		//��ѭ���������
		while(true){
			Thread.sleep(10000);
		}
	}
}
