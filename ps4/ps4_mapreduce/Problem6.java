/*
 * Problem6.java
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

public class Problem6 {
    public static class MyMapper
      extends Mapper<Object, Text, IntWritable, IntWritable> 
    {
        public void map(Object key, Text value, Context context)
          throws IOException, InterruptedException 
        {
            String line = value.toString(); 

            String[] fields = line.split(",");
            
            int groupIndex = 0;

            int birthYear = Integer.parseInt(fields[3].split("-")[0]);
            
            // Check if has enough fields to possibly contain group IDs
            if (fields.length >= 5) {
                if (fields[4].contains("@")) {
                    // Email address is present, check if groups are present
                    if (fields.length == 6 && !line.contains(";")) {
                        groupIndex = 5;
                    }
                    else if (fields.length > 6) {
                        groupIndex = 5;
                    }
                } 
                else {
                    // Email address is absent, check if groups are present
                    if (fields.length == 5 && !line.contains(";")) {
                        groupIndex = 4;
                    }
                    else if (fields.length > 5) {
                        groupIndex = 4;
                    }
                }  
                for (int i = groupIndex; i < fields.length; i++) { 
                    // If field has a semicolon, split the field and use value before it to write the key, value
                    if (fields[i].contains(";")) { 
                        // Check for correct age
                        if (birthYear <= 1963) {
                            context.write(new IntWritable(Integer.parseInt(fields[i].split(";")[0])), new IntWritable(1));
                        }
                        else { 
                            context.write(new IntWritable(Integer.parseInt(fields[i].split(";")[0])), new IntWritable(0));
                        }
                        return;
                    }
                    // Write the key,value
                    else { 
                        // Check for correct age
                        if (birthYear <= 1963) { 
                            context.write(new IntWritable(Integer.parseInt(fields[i])), new IntWritable(1));
                        } 
                        else { 
                            context.write(new IntWritable(Integer.parseInt(fields[i])), new IntWritable(0));
                        }
                        
                    }
                    
                } 
            }
            else { 
                return;
            }  
        }
    }


    public static class MyReducer
      extends Reducer<IntWritable, IntWritable, IntWritable, LongWritable> 
    {
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException 
      {
          int count = 0;
          for (IntWritable value : values) {
              count += value.get();
          }
          context.write(key, new LongWritable(count));
      }   
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "problem 6");
        job.setJarByClass(Problem6.class);

        // Specifies the names of the mapper and reducer classes.
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        // Sets the types for the keys and values output by the reducer.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(LongWritable.class);

        // Sets the types for the keys and values output by the mapper.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        // Configure the type and location of the data being processed.
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));

        // Specify where the results should be stored.
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
