package com.et;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZkTest {
	public static void main(String[] args) throws InterruptedException {
		//连接zookeeper一台达不到读写分离多个机器用逗号隔开
		String zkUrl="localhost:2181";
		//10000超时时间  connectionTimeout：50000自动连接不到五秒后自动连接
		ZkClient zk=new ZkClient(zkUrl,10000,50000);
		if(!zk.exists("/user")){
			//创建一个永久节点/user
			zk.createPersistent("/user");
			//创建两个顺序节点   EPHEMERAL_SEQUENTIAL临时的顺序节点  永久的顺序节点PERSISTENT_SEQUENTIAL
			//返回顺序节点真实的名字
			String znodeName=zk.create("/user/ls","boy", CreateMode.PERSISTENT_SEQUENTIAL);
			String znodeName1=zk.create("/user/ls","boy", CreateMode.PERSISTENT_SEQUENTIAL);
			System.out.println(znodeName);
			System.out.println(znodeName1);
			//创建一个临时节点
			zk.createEphemeral("/user/zs", "girl");
			//让主线程休眠就不会退出
			//Thread.sleep(Integer.MAX_VALUE);
		}
		//默认会结束
		zk.subscribeDataChanges("/db", new IZkDataListener() {
			
			//db节点删除时触发
			public void handleDataDeleted(String path) throws Exception {
				System.out.println(path);

			}
			
			//db节点修改时触发  path:路径  data值
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println(path);
			}
		});
		
		//死循环不会结束
		while(true){
			Thread.sleep(10000);
		}
	}
}
