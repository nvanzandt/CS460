/*
 * Problem4.java
 * 
 * CS 460: Problem Set 4
 */

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class Problem4 {
    /*** mapper and reducer for the first job in the chain */
    public static class MyMapper1
      extends Mapper<Object, Text, Text, IntWritable> 
    {
      public void map(Object key, Text value, Context context)
          throws IOException, InterruptedException 
        {
            // Convert Text object to a String
            String line = value.toString();

            // Split line by commas 
            String[] fields = line.split(",");
            
            if (fields.length >= 5) {
                String email = fields[4];
                // Check if the email is valid
                if (email.matches(".+@.+\\..+")) { 
                    String domain = email.split("@|;")[1];
                    context.write(new Text(domain), new IntWritable(1));
                }
            }
        }
    }

    public static class MyReducer1
      extends Reducer<Text, IntWritable, Text, LongWritable> 
    {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
          throws IOException, InterruptedException 
        {
            // Count the number of users for each domain
            long count = 0;
            for (IntWritable val : values) {
                count += val.get();
            }

            context.write(key, new LongWritable(count));
        }
    }

    /*** mapper and reducer for the second job in the chain */
    public static class MyMapper2
      extends Mapper<Object, Text, Text, Text> 
    {
        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException
        {
        
            String line = value.toString();

            // Write to context: constant, (domain, num users)
            context.write(new Text("domain sum"), new Text(line));
        }
    }

    public static class MyReducer2
      extends Reducer<Text, Text, Text, LongWritable> 
    {
        public void reduce(Text key, Iterable<Text> values,
            Context context) throws IOException, InterruptedException
        {
            // Find the domain with the most users
            long maxUsers = 0;
            String maxDomain = "";

            for (Text val : values) {
                String line = val.toString();
                String[] fields = line.split("\t");
                String domain = fields[0];
                long numUsers = Long.parseLong(fields[1]);

                // If applicable, update the domain with the most users
                if (numUsers > maxUsers) {
                    maxUsers = numUsers;
                    maxDomain = domain;
                }
            }

            context.write(new Text(maxDomain), new LongWritable(maxUsers));
        }
    }

    public static void main(String[] args) throws Exception {
        /*
         * First job in the chain of two jobs
         */
        Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf, "problem 4-1");
        job1.setJarByClass(Problem4.class);

        // Specifies the names of the first job's mapper and reducer classes.
        job1.setMapperClass(MyMapper1.class);
        job1.setReducerClass(MyReducer1.class);

        // Sets the types for the keys and values output by the first reducer.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(LongWritable.class);

        // Sets the types for the keys and values output by the first mapper.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        //  job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(IntWritable.class);

        // Configure the type and location of the data processed by job1.
        job1.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job1, new Path(args[0]));

        // Specify where job1's results should be stored.
        FileOutputFormat.setOutputPath(job1, new Path(args[1]));

        job1.waitForCompletion(true);

        /*
         * Second job the chain of two jobs
         */
        conf = new Configuration();
        Job job2 = Job.getInstance(conf, "problem 4-2");
        job2.setJarByClass(Problem4.class);

        // Specifies the names of the first job's mapper and reducer classes.
        job2.setMapperClass(MyMapper2.class);
        job2.setReducerClass(MyReducer2.class);

        // Sets the types for the keys and values output by the second reducer.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);

        // Sets the types for the keys and values output by the second mapper.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        // job2.setMapOutputKeyClass(Object.class);
        job2.setMapOutputValueClass(Text.class);

        // Configure the type and location of the data processed by job2.
        // Note that its input path is the output path of job1!
        job2.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job2, new Path(args[1]));
        FileOutputFormat.setOutputPath(job2, new Path(args[2]));

        job2.waitForCompletion(true);
    }
}
