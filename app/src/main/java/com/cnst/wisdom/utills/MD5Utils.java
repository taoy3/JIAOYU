package com.cnst.wisdom.utills;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author tangbinfeng.
 * @date 2016/2/26
 * @des [一句话描述]
 * @since [产品/模版版本]
 */

public class MD5Utils
{
    public static String Md5(String plainText)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for(int offset = 0; offset<b.length; offset++)
            {
                i = b[offset];
                if(i<0)
                {
                    i += 256;
                }
                if(i<16)
                {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            String md5 = buf.toString().substring(8, 24);
            //            System.out.println("result: " + buf.toString());//32位的加密
            //
            //            System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
            return md5;
        }catch(NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
