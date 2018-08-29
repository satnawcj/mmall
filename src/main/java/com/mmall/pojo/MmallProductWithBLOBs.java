package com.mmall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MmallProductWithBLOBs extends MmallProduct {

    private String subImages;

    private String detail;

}