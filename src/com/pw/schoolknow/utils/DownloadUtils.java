package com.pw.schoolknow.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class DownloadUtils {
        
        private URL url = null; 
        
        /**
         * ����URL�����ļ�,ǰ��������ļ����е��������ı�,�����ķ���ֵ�����ı����е�����
         * 1.����һ��URL����
         * 2.ͨ��URL����,����һ��HttpURLConnection����
         * 3.�õ�InputStream
         * 4.��InputStream���ж�ȡ����
         * @param urlStr
         * @return
         */
        public String download(String urlStr){
                StringBuffer sb = new StringBuffer();
                String line = null;
                BufferedReader buffer = null;
                try {
                        url = new URL(urlStr);
                        HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
                        buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while( (line = buffer.readLine()) != null){
                                sb.append(line);
                        }
                        
                } 
                catch (Exception e) {
                        e.printStackTrace();
                }
                finally{
                        try {
                                buffer.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return sb.toString();
        }

        /**
         * 
         * @param urlStr
         * @param path
         * @param fileName
         * @return 
         * -1:�ļ����س���
         * 0:�ļ����سɹ�
         * 1:�ļ��Ѿ�����
         */
        public int downFile(String urlStr,String path,String fileName){
                InputStream inputStream = null;
                try {
                    FileUtils fileUtils = new FileUtils();                        
                    if(fileUtils.isFileExist(path + fileName)){
                            return 1;
                    } else {
                        inputStream = getInputStreamFromURL(urlStr);
                        File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);
                        if(resultFile == null){
                                return -1;
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }finally{
                    try {
                         inputStream.close();
                    }catch (IOException e) {
                         e.printStackTrace();
                    }
                }
                return 0;
        }
        
        /**
         * ����URL�õ�������
         * @param urlStr
         * @return
         */
        public InputStream getInputStreamFromURL(String urlStr) {
                HttpURLConnection urlConn = null;
                InputStream inputStream = null;
                try {
                        url = new URL(urlStr);
                        urlConn = (HttpURLConnection)url.openConnection();
                        inputStream = urlConn.getInputStream();
                        
                } catch (MalformedURLException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                
                return inputStream;
        }
        
        
        /**
         * �����ļ�
         * @param filePath  �����ļ��ĵ�ַ
         * @param savePath  �����·��
         */
        public void downFile(String filePath,String savePath){
        	InputStream in = null;
        	HttpGet http = new HttpGet(filePath);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = client.execute(http);
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(response.getEntity());
				in = bufferedHttpEntity.getContent();
			} catch (Exception e) {
				if (in != null)
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
			File file = new File(savePath);
			String basePath = file.getParent();
			File basePathFile = new File(basePath);
			if (!basePathFile.exists()) {
				basePathFile.mkdirs();
			}
			try {
				file.createNewFile();
				@SuppressWarnings("resource")
				FileOutputStream fileout = new FileOutputStream(file);
				byte[] buffer = new byte[4*1024];
				while (in.read(buffer) != -1) {
					fileout.write(buffer);
				}
				fileout.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
        }
}

