package com.jspgou.common.upload;

import java.io.*;
/**
 * 文件存储接口
 * 
 * @author liufang
 * This class should preserve.
 * @preserve
*/
public interface Repository
{

    public boolean store(String s, InputStream inputstream)
        throws IOException;

    public boolean retrieve(String s, OutputStream outputstream);
}
