package com.jmd.web.service;

public interface TileService {

    byte[] getTileImageByteLocal(int z, int x, int y);

    byte[] getTileImageByteByProxy(int z, int x, int y, String url);

}
