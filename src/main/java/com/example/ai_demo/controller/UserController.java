package com.example.ai_demo.controller;

import com.example.ai_demo.aip.GeneralBasic;
import com.example.ai_demo.domain.User;
import com.example.ai_demo.repository.UserDao;
import com.example.ai_demo.service.UserService;
import com.example.ai_demo.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    private GeneralBasic Basic;

    RestTemplate restTemplate = new RestTemplate ();
    String FaceURL = "http://region-41.seetacloud.com:54525/";
    String StableURL = "http://region-42.seetacloud.com:59533/";
    @PostMapping("/login")
    public Result<User> loginController(@RequestParam String uname, @RequestParam String password) throws InterruptedException {
        User user = userService.loginService(uname, password);
        if(user!=null){
            return Result.success(user,"登录成功！");
        }else{
            return Result.error("123","账号或密码错误！");
        }
    }

    @GetMapping("/getURL")
    public Result<ArrayList<String>> getURLController() throws InterruptedException {
        ArrayList<String> Res = new ArrayList<>();
        Res.add(FaceURL);
        Res.add(StableURL);
        return Result.success(Res, "获取成功");
    }

    @PostMapping("/register")
    public Result<User> registController(@RequestBody User newUser){
        User user = userService.registService(newUser);
        if(user!=null){
            return Result.success(user,"注册成功！");
        }else{
            return Result.error("456","用户名已存在！");
        }
    }


    @PostMapping("/change")
    public Result<User> changePasswordController(@RequestParam String uname, @RequestParam String oldpass, @RequestParam String password){
        User user = userService.changeService(uname, oldpass, password);
        if (user==null){
            return Result.error("123","账号或密码错误！");
        }
        else{
            user.setPassword(password);
            return Result.success(user,"修改成功！");
        }
    }

    @PostMapping("/excavator")
    public Result<User> saveAvatorController(@RequestParam(value = "uname") String uname, @RequestParam(value="file") MultipartFile image) throws InterruptedException, IOException {
        User user = userDao.findByUname(uname);
        if(user==null){
            return Result.error("789","不存在的账号");
        }
        if (!image.isEmpty()){
            String basedir = new File("").getAbsolutePath();
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
            String filePath ="/Users/"+uname+"/avator/" + fileName;
            user.setAvator(filePath);
            try {
                userDao.save(user);
                filePath = basedir + filePath;
                File file = new File(filePath);
                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                OutputStream os = new FileOutputStream(file);
                os.write(image.getBytes());
                //System.out.println(directory.getCanonicalPath() + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user.setPassword("");
        return Result.success(user,"上传成功");
    }

    @PostMapping("/saveFaceImg")
    @ResponseBody
    public Result<String> saveFaceImgController(@RequestParam(value = "file") List<MultipartFile> files, @RequestParam(value = "uname") String uname) throws InterruptedException, IOException {
        User user = userDao.findByUname(uname);
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        if(user==null){
            return Result.error("789","不存在的账号");
        }
        try{
            bodyParams.add("uname",uname);
            for (MultipartFile file : files) {
                ByteArrayResource resource = new ByteArrayResource(file.getBytes()){
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                bodyParams.add("files", resource);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyParams, headers);
            String result= restTemplate.postForEntity(FaceURL+"saveFaceImg", requestEntity, String.class).getBody();
            if (result.equals("success")){
                System.out.println(result);
                return Result.success("success","录入人脸数据成功");
            }
            //return Result.success("success","录入人脸数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("102","录入数据发生错误");
    }

    @PostMapping("/videoLogin")
    public Result<User> faceVideoLoginController(@RequestParam(value="file") MultipartFile image) throws InterruptedException, IOException {
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        try{
            ByteArrayResource resource = new ByteArrayResource(image.getBytes()){
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            bodyParams.add("image", resource);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyParams, headers);
            String result= restTemplate.postForObject(FaceURL+"recgnizeface", requestEntity, String.class);
            System.out.println(result);
            User user = userDao.findByUname(result);
            if (user!=null){
                user.setPassword("");
                return Result.success(user, "识别成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("102","录入数据发生错误");
    }
    /*
    @GetMapping("/getAvator")
    public byte[] ajaxFunction(@RequestParam String uname) throws Exception {
        User user = userDao.findByUname(uname);
        String imgSrc = user.getAvator();
        if (imgSrc == "default"){
            return null;
        }
        FileInputStream fin = new FileInputStream(imgSrc);
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes  = new byte[fin.available()];
        //将文件内容写入字节数组，提供测试的case
        fin.read(bytes);
        fin.close();
        System.out.println('1');
        return bytes;
    }
    */

    @GetMapping("/getAvator")
    public String ajaxFunction(@RequestParam String uname) throws Exception {
        User user = userDao.findByUname(uname);
        String imgSrc = user.getAvator();
        if (imgSrc.equals("default")){
            return null;
        }
        return imgSrc;
    }

    @PostMapping("/postTextImage")
    public Result<String> postTextImage(@RequestParam String uname, @RequestParam(value="file") MultipartFile image) throws Exception {
        User user = userDao.findByUname(uname);
        String basedir = new File("").getAbsolutePath();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        String filePath ="/Users/"+uname+"/textImage/" + fileName;
        String Parentpath = basedir+ "/Users/"+uname+"/textImage/";
        filePath = basedir + filePath;
        File file = new File(filePath);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        else {
            String[] content = file.getParentFile().list();
            for (String name : content) {
                File temp = new File(Parentpath, name);
                temp.delete();
            }
        }
        OutputStream os = new FileOutputStream(file);
        os.write(image.getBytes());
        String result = Basic.generalBasic(filePath);
        return Result.success(result,"123");
    }

}