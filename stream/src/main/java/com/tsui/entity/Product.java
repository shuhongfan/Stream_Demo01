/*
 * @author tsui
 * @date 2022/2/19 20:27
 */
package com.tsui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product{
    int id;
    String name;
    Double price;
}
