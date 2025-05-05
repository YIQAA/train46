package com.qdu.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/train", "root", "root")
                .globalConfig(builder -> {
                    builder.author("徐增润")
                            .outputDir("src/main/java")
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent("com.qdu")
                            .entity("entity")
                            .mapper("mapper")
                            .controller("controller")
                            .service("service")
                            .serviceImpl("service.impl");
                })
//                "users", "station","train","daily_train", "route","seat_config","seat",
//                                    "orders","seat_occupancy","customer_service_sessions","knowledge_base"
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "feedback") // 指定表名
                            .entityBuilder()
                            .enableLombok()
                            .controllerBuilder()
                            .enableRestStyle();
                })
                // 使用 Freemarker引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}