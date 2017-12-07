package com.bjbsh.linford.bookapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bjbsh.linford.bookapp.Exception.BookException;
import com.bjbsh.linford.bookapp.dao.BookDaoHelper;
import com.bjbsh.linford.bookapp.dao.ClassifyDaoHelper;
import com.bjbsh.linford.bookapp.entity.BookEntity;
import com.bjbsh.linford.bookapp.entity.ClassifyEnity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加书籍信息页面
 */
public class AddBookActivity extends AppCompatActivity {

    //定義一個讀取本地文件的返回值,用於進出的判斷
    private static final int FILE_SELECT_CODE = 001;

    //定义界面布局控件
    @BindView(R.id.addBookToolbar)
    Toolbar addBookToolbar;
    @BindView(R.id.fileSelectButton)
    Button fileSelectButton;
    @BindView(R.id.fileName)
    TextView fileName;
    @BindView(R.id.bookImage_add)
    SimpleDraweeView bookImageAdd;
    @BindView(R.id.addBookName)
    EditText addBookName;
    @BindView(R.id.paperBook)
    RadioButton paperBook;
    @BindView(R.id.eBook)
    RadioButton eBook;
    @BindView(R.id.bookTypeGroup)
    RadioGroup bookTypeGroup;
    @BindView(R.id.classifySpinner)
    Spinner classifySpinner;
    @BindView(R.id.addBookButton)
    Button addBookButton;



    //定义BookEntity数据库管理类
    private BookDaoHelper bookDaoHelper;
    //定义字符串存取值
    private String bookName, bookTypeName, bookClassifyName, bookImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册Fresco图片框架
        Fresco.initialize(this);
        setContentView(R.layout.activity_add_book);
        //注册注解绑定Id事件
        ButterKnife.bind(this);
        initView();
        toolBarEvent();
    }

    /**
     * 点击返回按钮返回主页
     */
    public void toolBarEvent() {
        addBookToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * 实例化控件
     */
    private void initView() {
        bookDaoHelper = new BookDaoHelper(this);
        //Spinner控件处理
        setClassifySpinner();
    }

    /**
     * 获取下拉菜单的分类名
     * 先从数据库获取分类数据
     * 再获取下拉菜单选中的数据
     */
    private void setClassifySpinner() {
        //数据库操作
        ClassifyDaoHelper classifyDao = new ClassifyDaoHelper(this);
        //获取数据
        List<ClassifyEnity> classifyEnityList = classifyDao.queryAll();
        //装数据的容器
        final List<String> spinnerData = new ArrayList<String>();
        //遍历数据
        for (ClassifyEnity entity : classifyEnityList) {
            spinnerData.add(entity.getClassifyName());
        }
        //定义Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerData);
        //加载适配器样式
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //加载适配器到Spinner
        classifySpinner.setAdapter(adapter);
        //定义Spinner标题1
        classifySpinner.setPrompt("请选择一个分类:");
        //添加item点击事件
        classifySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookClassifyName = adapter.getItem(i);
                setToast(bookClassifyName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * 文件选择和添加按钮点击事件
     *
     * @param view
     */
    @OnClick({R.id.fileSelectButton, R.id.addBookButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fileSelectButton:
              /* 第三方文件选择器的使用
               new LFilePicker().withActivity(AddBookActivity.this)
                        .withRequestCode(Constant.REQUESTCODE_FROM_ACTIVITY)
                        .withFileFilter(new String[]{".jpg", ".png",".jpeg"})
                        .withTitle("文件选择")//标题文字
                        .withTitleColor("#FF99CC")//文字颜色
                        .start();*/

                //系统默认文件选择器选择图片的使用
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, FILE_SELECT_CODE);

                // 直接调用相册选择图片
              /*  Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, FILE_SELECT_CODE);
*/
                break;
            case R.id.addBookButton:
                bookDaoHelper.deleteBookAll();
                try {
                    getBookData();
                } catch (BookException e) {
                    e.printStackTrace();
                    setToast("这本书籍已经添加过了");
                }
                setToast("添加书籍成功!");
                List<BookEntity> bookEntityList = bookDaoHelper.queryBookAll();
                System.out.println(bookEntityList.toString());
                break;
        }
    }


    /**
     * 接收手机文件选择返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == FILE_SELECT_CODE) {
//            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
//            String string =uri.toString();
//            File file;
//            String a[]=new String[2];
//            //判断文件是否在sd卡中
//            if (string.indexOf(String.valueOf(Environment.getExternalStorageDirectory()))!=-1){
//                //对Uri进行切割
//                a = string.split(String.valueOf(Environment.getExternalStorageDirectory()));
//                //获取到file
//                file = new File(Environment.getExternalStorageDirectory(),a[1]);
//
//            }else if(string.indexOf(String.valueOf(Environment.getDataDirectory()))!=-1){ //判断文件是否在手机内存中
//                //对Uri进行切割
//                a =string.split(String.valueOf(Environment.getDataDirectory()));
//                //获取到file
//                file = new File(Environment.getDataDirectory(),a[1]);
//            }else{
//                //出现其他没有考虑到的情况
//                setToast("文件路径解析失败！");
//                return;
//            }
//            setToast(file.toString());
            setToast("hello world");
            //获取图片路径
            Uri imageUri = data.getData();
            bookImageAdd.setImageURI(imageUri);
            //   Uri imageUri = data.getData();
//                String imagePath = FileUtils3.getPath(this, selectedImage);
//                String imagePath2 = FileUtils.getPath(this, selectedImage);
//                String imagePath3 = FileUtils.getPath2(this, selectedImage);
//                String imagePath4 = FileUtils.getRealFilePath(this, selectedImage);
//                String imagePath5 = FileUtils2.getImageAbsolutePath(this, selectedImage);
//                String imagePath7 = FileUtils2.getImagePath(this, selectedImage);
//                File file = new File(imagePath);
//                Log.e("uri", "===================>" + selectedImage);
//                Log.e("getPath", "==================>" + selectedImage.getPath());
//                Log.e("imagePath", "=================>" + imagePath);
//                Log.e("imagePath2", "=================>" + imagePath2);
//                Log.e("imagePath3", "=================>" + imagePath3);
//                Log.e("imagePath4", "=================>" + imagePath4);
//                Log.e("imagePath5", "=================>" + imagePath5);
//                Log.e("imagePath7", "=================>" + imagePath7);
//                //Uri uri = FileUtils2.getImageContentUri(this, new File("file://" + imagePath));
//                Log.e("formatUri", "=========================>" + Uri.fromFile(new File("file://" + imagePath3)));
            bookImageUrl = imageUri.toString();
            setToast(bookImageUrl);
            // Uri uri=Uri.parse(bookImageUrl2);
            // bookImage.setImageURI(imageUri);
            // String uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501359296468&di=7c7990e06a88c64a1e6ec6212250aa4d&imgtype=0&src=http%3A%2F%2Fimg2.niutuku.com%2F1312%2F0800%2F0800-niutuku.com-14339.jpg";

            //showFileImage(bookImage, bookImageUrl);

        }
    }

    /**
     * 显示本地图片。
     *
     * @param draweeView imageView。
     * @param path       路径。
     */
    public static void showFileImage(SimpleDraweeView draweeView, String path) {
        try {
            Uri uri = Uri.parse(path);
            draweeView.setImageURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把数据封装到BookEntity并插入数据库
     */
    public void getBookData() throws BookException{
        //获取默认的bookType的数据
        bookTypeName = paperBook.getText().toString();
        //获取改变后的type类型数据(该事件只有在改变RadioButton才会触发)
        bookTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton bookType = (RadioButton) findViewById(radioButtonId);
                bookTypeName = bookType.getText().toString();
                setToast(bookTypeName);
            }
        });
        //获取书籍名称内容
        bookName = addBookName.getText().toString();
        BookEntity book = new BookEntity(null, bookImageUrl, bookName, bookTypeName, 1, bookClassifyName);
      //  System.out.println(book.toString());
        if (bookDaoHelper.queryBookByName(bookName)!=null){
            System.err.print("..............................................");
            throw new BookException();
        }
        try {

            bookDaoHelper.insertBook(book);
        } catch (Exception e) {
            e.printStackTrace();
            setToast("输入的内容不能为空!");
        }
    }

    /**
     * 封装Toast提示
     *
     * @param string
     */
    private void setToast(String string) {
        Toast.makeText(AddBookActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //login_auto_account.setCursorVisible(false);
        imm.hideSoftInputFromWindow(addBookName.getWindowToken(), 0);
        return super.onTouchEvent(event);
    }
}
