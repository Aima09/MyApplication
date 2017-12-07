package com.example.greendaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.linford.greendao.gen.UserDao;
import com.linfrod.greendao.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试GreenDao的使用:增删改查
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private EditText nameEt, ageEt, sexEt, salaryEt;
    private Button insertButton, queryButton, updateButton, deleteButton;
    private ListView listView;
    private String nameText, ageText, sexText, salaryText;
    private List<User> userList = new ArrayList<User>();
    private UserDao userDao;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        listView = (ListView) findViewById(R.id.listView);
        userDao = new GreenDaoManager(MainActivity.this).getNewSession().getUserDao();
        nameEt = (EditText) findViewById(R.id.nameEt);
        ageEt = (EditText) findViewById(R.id.ageEt);
        sexEt = (EditText) findViewById(R.id.sexEt);
        salaryEt = (EditText) findViewById(R.id.salaryEt);
        insertButton = (Button) findViewById(R.id.insertButton);
        queryButton = (Button) findViewById(R.id.queryButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        insertButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
//        nameText = nameEt.getText().toString().trim();
//        ageText = ageEt.getText().toString().trim();
//        sexText = sexEt.getText().toString().trim();
//        salaryText = salaryEt.getText().toString().trim();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.insertButton:
                String name = nameEt.getText().toString().trim();
                String age = ageEt.getText().toString().trim();
                String sex = sexEt.getText().toString().trim();
                String salary = salaryEt.getText().toString().trim();
                userData = new User(null, name, age, sex, salary);

                long result = insertUser(userData);
                if (result > 0) {
                    System.out.println("添加记录成功...");
                }
                break;
            case R.id.queryButton:
                String name2 = nameEt.getText().toString().trim();
                String age2 = ageEt.getText().toString().trim();
                String sex2 = sexEt.getText().toString().trim();
                String salary2 = salaryEt.getText().toString().trim();
                userData = new User(null, name2, age2, sex2, salary2);

                queryData(userData);
                break;
            case R.id.updateButton:
                break;
            case R.id.deleteButton:
                break;
        }
    }

    /**
     * 插入一条数据
     *
     * @param user
     * @return
     */
    public long insertUser(User user) {

        userList.add(userData);
        listView.setAdapter(new MyAdapter(userList, this));
        return userDao.insert(userData);
    }

    /**
     * 查询全部记录
     *
     * @param user
     */
    public void queryData(User user) {

            userList = userDao.loadAll();
            for (User user3 :
                    userList) {
                System.out.println(user3.toString());

            }
            listView.setAdapter(new MyAdapter(userList, MainActivity.this));

//        else{
//            userList=userDao.queryRaw("UserDao.Properties.Name=?",user.getName());
//            listView.setAdapter(new MyAdapter(userList,this));
//        }

      /*  if (user == null) {
            userList = userDao.loadAll();
            System.out.println(userList.toString());
            listView.setAdapter(new MyAdapter(userList, MainActivity.this));
        } else if (!(user.getName().equals(""))) {
            userList = userDao.queryRaw(user.getName());
            listView.setAdapter(new MyAdapter(userList, MainActivity.this));
        } else if (!(user.getName().equals("")) && !(user.getAge().equals(""))) {
            userList = userDao.queryRaw(user.getName(), user.getAge());
            listView.setAdapter(new MyAdapter(userList, MainActivity.this));
        } else if (!(user.getName().equals("")) && !(user.getAge().equals("")) && !(user.getSex().equals(""))) {
            userList = userDao.queryRaw(user.getName(), user.getAge(), user.getSex());
            listView.setAdapter(new MyAdapter(userList, MainActivity.this));
        } else if (!(user.getName().equals("")) && !(user.getAge().equals("")) && !(user.getSex().equals("")) && !(user.getSalary().equals(""))) {
            userList = userDao.queryRaw(user.getName(), user.getAge(), user.getSex(), user.getSalary());
            listView.setAdapter(new MyAdapter(userList, MainActivity.this));
        }
*/
    }

    public int deleteData(User user) {

        return 0;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        return false;
    }
}
