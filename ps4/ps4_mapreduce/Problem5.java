/*
 * Problem5.java
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

public class Problem5 {
    public static class MyMapper
      extends Mapper<Object, Text, Text, Text> 
    {
        public void map(Object key, Text value, Context context)
          throws IOException, InterruptedException 
        {
		
			String line = value.toString();

			// User ID is first element of String array of values
			int user_id = Integer.parseInt(line.split(",")[0]);

			// If the user has no friends
			int numFriends = 0;

			// If there is a semicolon, then the user has friends
			if (line.contains(";")) { 
				// Split the line by semicolons, take the second element, and split it by commas
				String[] friends = line.split(";")[1].split(",");
				numFriends = friends.length;
			}
			context.write(new Text("friend sum"), new Text(user_id + "," + numFriends));
        }
    }
	
    public static class MyReducer
      extends Reducer<Text, Text, IntWritable, IntWritable> 
    {
      public void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException 
      {
		
		int maxFriends = 0;
		int maxId = 0;

		for (Text val : values) { 
			String line = val.toString(); 
			String[] fields = line.split(","); 
			int id = Integer.parseInt(fields[0]);
			int numFriends = Integer.parseInt(fields[1]);

			if (numFriends > maxFriends) { 
				maxFriends = numFriends;
				maxId = id;
			}
		}

		context.write(new IntWritable(maxId), new IntWritable(maxFriends));
		}
	}

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "problem 5");
        job.setJarByClass(Problem5.class);

        // Specifies the names of the mapper and reducer classes.
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        // Sets the types for the keys and values output by the reducer.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        // Sets the types for the keys and values output by the mapper.
        /* CHANGE THE CLASS NAMES AS NEEDED IN THESE TWO METHOD CALLS */
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // Configure the type and location of the data being processed.
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));

        // Specify where the results should be stored.
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
