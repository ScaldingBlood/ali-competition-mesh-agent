package com.alibaba.dubbo.performance.demo.agent.dubbo.model;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ken.lj
 * @date 02/04/2018
 */
public class JsonUtils {

    public static void writeObject(Object obj, PrintWriter writer) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.write(obj);
        out.writeTo(writer);
        out.close(); // for reuse SerializeWriter buf
        writer.println();
        writer.flush();
    }

    public static void writeBytes(byte[] b, PrintWriter writer) {
        writer.print(new String(b));
        writer.flush();
    }

    public static void writeRpcInvocation(RpcInvocation inv, PrintWriter writer) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);

        serializer.write(inv.getAttachment("dubbo", "2.0.1"));
        serializer.write("\n");
        serializer.write(inv.getAttachment("path"));
        serializer.write("\n");
        serializer.write(inv.getAttachment("version"));
        serializer.write("\n");
        serializer.write(inv.getMethodName());
        serializer.write("\n");
        serializer.write(inv.getParameterTypes());
        serializer.write("\n");
        serializer.write(new String(inv.getArguments()));
        serializer.write(inv.getAttachments());

        out.writeTo(writer);
        out.close(); // for reuse SerializeWriter buf
        writer.println();
        writer.flush();
    }
}
