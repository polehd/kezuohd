/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.util;

/**
 *
 * @author zlzuo
 */
public class FloatToHex2 {

    public static void main(String[] args) {
      
        try {
            String sum = "33431445";
            System.out.print("String（33431445） to float : ");
            System.out.println(Float.intBitsToFloat(Integer.valueOf(sum.trim(), 16)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String sum2 = "45144333";
            System.out.print("String（45144333） to float : ");
            System.out.println(Float.intBitsToFloat(Integer.valueOf(sum2.trim(), 16)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
