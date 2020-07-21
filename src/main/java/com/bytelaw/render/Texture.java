package com.bytelaw.render;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int id;

    public Texture(String fileName) throws Exception {
        this(loadTexture(fileName));
    }

    public Texture(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static int loadTexture(String fileName) throws Exception {
        int width, height;
        ByteBuffer buf;

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer w = memoryStack.mallocInt(1);
            IntBuffer h = memoryStack.mallocInt(1);
            IntBuffer channels = memoryStack.mallocInt(1);

            InputStream is = Texture.class.getResourceAsStream(fileName);
            byte[] data = inputStreamToByteArray(is);
            ByteBuffer isBuf = MemoryUtil.memAlloc(data.length);
            isBuf.put(data).flip();

            buf = stbi_load_from_memory(isBuf, w, h, channels, 4);
            if(buf == null)
                throw new Exception(String.format("Unable to load image from path [%s], reason: %s", fileName, stbi_failure_reason()));

            width = w.get();
            height = h.get();
            MemoryUtil.memFree(isBuf);
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(buf);
        return textureId;
    }

    private static byte[] inputStreamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}
