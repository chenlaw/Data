package com.example.demo.po;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/27 22:02
 */
@Data
public class Accounts {
    String acc;
    String passwd;
    String permission;
}
