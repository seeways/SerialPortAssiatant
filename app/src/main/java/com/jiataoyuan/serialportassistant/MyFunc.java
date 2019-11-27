package com.jiataoyuan.serialportassistant;

/**
 * 数据转换工具
 * @author TaoYuan
 */
class MyFunc {
    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    private static int isOdd(int num) {
        return num & 0x1;
    }

    //-------------------------------------------------------
    private static byte HexToByte(String inHex)//Hex字符串转byte
    {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    private static String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    //-------------------------------------------------------
    static String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        for (byte b : inBytArr) {
            strBuilder.append(Byte2Hex(b));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //转hex字符串转字节数组
    static byte[] HexToByteArr(String inHex)//hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {//奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {//偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }
}