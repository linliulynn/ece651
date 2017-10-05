package com.sharkjob.Dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.sharkjob.controller.IndexController;
import com.sharkjob.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Chino on 2017/9/28.
 */
@Data
public class UserDao {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private DynamoDBMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);


    public void createSharkJobUserTable(){
        try {
            CreateTableRequest req = userMapper.generateCreateTableRequest(User.class);
            // Table provision throughput is still required since it cannot be specified in your POJO
            req.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
            // Fire off the CreateTableRequest using the low-level client
            dynamoDBClient.createTable(req);
        } catch (ResourceInUseException e) {
            //swallow
            log.info("Table has already exist.");
        }
    }

    public boolean saveUserInSharkJobUserTable(User user) {
        if (findUserInSharkJobUserTableThroughEmail(user.getEmail()) == null) {
            userMapper.save(user);
            log.info("User with email: {} has successfully registered",user.getEmail());
            return true;
        }
        else {
            log.info("User with email: {} has already registered",user.getEmail());
            return false;
            //ask user to change an email...
        }
    }

    public void deleteUserInSharkJobUserTable(String email){
        userMapper.delete(findUserInSharkJobUserTableThroughEmail(email));
    }

    public User findUserInSharkJobUserTableThroughEmail(String email){
        User user = userMapper.load(User.class, email);
        if (user != null) {
            log.info(user.toString());
        }
        return user;
    }

    //Other find operations.

    //find through username...

    //find through skills? not sure how to arrange the "skills"(list? string? hash map?)
    // Need to find a data structure good for query.
    // ...This should be discussed in the future.
    //Test should be added.

}