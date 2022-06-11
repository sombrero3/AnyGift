package com.example.anygift.knn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

 public class KNN {
    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }
     public static String toClassify="./src/toClassify.txt";
      static public void knn()  {
        BufferedReader datafile = KNN.readDataFile("./src/data.txt");
        BufferedReader toClass = KNN.readDataFile(toClassify);

          Instances data = null, dataToClass=null;
          try {
              data = new Instances(datafile);
              dataToClass= new Instances(toClass);

          } catch (IOException e) {
              e.printStackTrace();
          }
          data.setClassIndex(data.numAttributes() - 1);

        dataToClass.setClassIndex(dataToClass.numAttributes()-1);

        Instance first = dataToClass.instance(0);
        Instance sec = dataToClass.instance(1);
    /*    Instance second = data.instance(1);
        Instance third = data.instance(2);

        data.delete(0);
        data.delete(1);
        data.delete(2);
       */ Classifier ibk = new IBk(3);
          try {
              ibk.buildClassifier(data);
          } catch (Exception e) {
              e.printStackTrace();
          }

          double class1 = 0;
          try {
              class1 = ibk.classifyInstance(first);
          } catch (Exception e) {
              e.printStackTrace();
          }
          int num=(int)class1;
        switch(num) {
            case 0:
                System.out.println("first: " + "A");
                break;
            case 1:
                System.out.println("first: " + "B");
                break;
            case 2:
                System.out.println("first: " + "C");
                break;
            default:
                System.out.println("first: " + "G");
                break;

        }
          double class2 = 0;
          try {
              class2 = ibk.classifyInstance(sec);
          } catch (Exception e) {
              e.printStackTrace();
          }
          int num2=(int)class2;
        switch(num2) {
            case 0:
                System.out.println("first: " + "A");
                break;
            case 1:
                System.out.println("first: " + "B");
                break;
            case 2:
                System.out.println("first: " + "C");
                break;
            default:
                System.out.println("first: " + "G");
                break;



        }

    }
}