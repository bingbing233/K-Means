package com.company;

/**
 * @author liangbinghao
 * @date 2020-6-1 21:57
 *（每次运行都会生成新的样本点，若想要固定的样本点，可在第二次运行时不执行createData方法）
 * 1、使用方法：在Run类的main方法里面新建KMeans对象，并依次调用createData、getData、sort方法
 * 2、首先会随机生成训练样本，写入文件data.txt，并读取保存在data数组里面
 * 3、用随机数随机选出K个初始的中心点
 * 4、计算所有点到K个中心点的距离，选出距离最近的那个点，将其归属于该中心点的一类，完成后会将样本点分成K类
 * 5、计算每一类样本点的平均值，平均值为每一类新的中心点
 * 6、重复4、5步骤，直到收敛（到达迭代次数或两次迭代得到的中心点一致）
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class KMeans {
    double[][] data = new double[500][2];//从文件中读取的数据集
    private int K;//分成几类？
    private int iterateTime;//迭代次数
    boolean isSameAsLastTime;//阈值，当两次找到的中心点一致时，停止迭代

    public KMeans(int k,int iterateTime) {
        K = k;
       this.iterateTime = iterateTime;
    }


    //开始分类
    public void sort() {
        double[][] center = new double[K][2];//中心点
        double[] distance = new double[K];//该点到N个中心点的距离

        //N个装着数组的列表
        ArrayList<ArrayList<double[]>> sortData = new ArrayList<>();

        //获取初始中心点
        for(int i=0;i<K;i++){
            center[i][0]=data[new Random().nextInt(100)][0];
            center[i][1]=data[new Random().nextInt(100)][1];
            sortData.add(new ArrayList<double[]>());
            System.out.println("初始"+i+"中心点："+center[i][0]+","+center[i][1]);
        }

        //首次分类
        for(int i=0;i<100;i++){
            int minIndex=0;
            double minDistance=999;
            for(int j=0;j<K;j++){
                //选出距离最小的种类
                distance[j] = getDistance(data[i][0],data[i][1],center[j][0],center[j][1]);
                if(distance[j]<minDistance){
                    minDistance = distance[j];
                    minIndex = j;
                }
              sortData.get(minIndex).add(new double[]{data[i][0],data[i][1]});
            }
        }

              //重复迭代
        while (iterateTime>0){
            //找到新的中心点并保存
            isSameAsLastTime = true;
            for(int i=0;i<K;i++){
                double x=0,y=0;
                for(int j=0;j<sortData.get(i).size();j++){
                    x+=sortData.get(i).get(j)[0];
                    y+=sortData.get(i).get(j)[1];
                }

                x = x/sortData.get(i).size();
                y = y/sortData.get(i).size();
                if(x!=center[i][0]||y!=center[i][1]){
                    isSameAsLastTime = false;
                }
                center[i][0]=x;
                center[i][1]=y;
                System.out.println("第"+i+"中心点---("+center[i][0]+","+center[i][1]+")"+"--------size="+sortData.get(i).size());
            }
            System.out.println("=========================================================");

            //清空原来存放分类的列表
            for(int i=0;i<K;i++){
                sortData.get(i).clear();
            }

            //使用新的中心点来进行分类
            for(int i=0;i<100;i++){
                int minIndex=0;
                double minDistance=999;
                for(int j=0;j<K;j++){
                    //选出距离最小的种类
                    distance[j] = getDistance(data[i][0],data[i][1],center[j][0],center[j][1]);
                    if(distance[j]<minDistance){
                        minDistance = distance[j];
                        minIndex = j;
                    }
                    sortData.get(minIndex).add(new double[]{data[i][0],data[i][1]});
                }
            }
            if(isSameAsLastTime){
                break;
            }
            iterateTime--;
        }

        //按照每个分类输出数据
        for(int i=0;i<K;i++){
            System.out.println("\n----------第"+i+"类"+"数据----------");
            for(int j=0;j<sortData.get(i).size();j++){
               System.out.print("("+sortData.get(i).get(j)[0]+","+ sortData.get(i).get(j)[1]+")\t");
                if(j%10==0){
                    System.out.println("\n");
                }
            }
        }
    }

    //计算点之间的欧式距离
    public double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    //从文件中读取样本点
    public void getData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null) {
            data[row][0] = Integer.valueOf(line.split(",")[0]);
            data[row][1] = Integer.valueOf(line.split(",")[1]);
            row++;
        }
    }

    //生成随机数对并输出到文件中
    public void createData() throws IOException {
        File file = new File("data.txt");
        FileWriter writer = new FileWriter(file);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            writer.write(random.nextInt(500) + ",");
            writer.write(random.nextInt(500) + "\n");

        }
        writer.close();

    }
}
