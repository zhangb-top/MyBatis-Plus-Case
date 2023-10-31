package com.skyblue.generator;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.skyblue.bo.CodeGeneratorBO;
import lombok.Data;

@Data
public class AutoGenerator {
    private final CodeGeneratorBO cgBO;

    private String outDir = "D:\\java\\promise\\mybatis-plus-case\\src\\main\\resources\\temp";

    public AutoGenerator(CodeGeneratorBO cgBO) {
        this.cgBO = cgBO;
    }

    /**
     * 生成代码核心逻辑
     */
    public void execute() {
        FastAutoGenerator.create(dataSourceBuilder())
                // 全局配置
                .globalConfig(this::globalConfigBuilder)
                // 包配置
                .packageConfig(this::packageConfigBuilder)
                // 策略配置
                .strategyConfig(this::strategyConfig)
                .execute();
    }

    private void strategyConfig(StrategyConfig.Builder builder) {
        // 表名称
        builder.addInclude(cgBO.getTableNames())
                // 字段前缀
                .addFieldPrefix(cgBO.getFieldPrefixes())
                // 排除表名
                .addExclude(cgBO.getExcludeTableNames())

                // entity类配置
                .entityBuilder()
                // 下滑线转驼峰
                .naming(NamingStrategy.underline_to_camel)
                .formatFileName(cgBO.getFileNamePatternEntity())
                // 只有当插入对象 ID 为空，才自动填充
                .idType(IdType.ASSIGN_ID)
                // 逻辑删除字段
                .logicDeleteColumnName(cgBO.getFieldLogicDelete())
                // 乐观锁字段
                .versionColumnName(cgBO.getFieldVersion())
                // 实体父类的全类名
                .superClass(cgBO.getSuperClassName())
                // 忽略的字段（也就是数据库中的字段不会出现在实体类中）
                .addIgnoreColumns(cgBO.getIgnoreColumns())

                // mapper文件配置
                .mapperBuilder()
                // mapper文件名格式
                .formatMapperFileName(cgBO.getFileNamePatternMapper())
                // mapper XML文件名格式
                .formatXmlFileName(cgBO.getFileNamePatternMapperXml())

                // service类配置
                .serviceBuilder()
                // service文件名格式
                .formatServiceFileName(cgBO.getFileNamePatternService())
                // serviceImpl文件名格式
                .formatServiceImplFileName(cgBO.getFileNamePatternServiceImpl());

        // 单独设置 true/false 字段
        Entity.Builder entityBuilder = builder.entityBuilder();
        if (BooleanUtil.isTrue(cgBO.getLombokChainModel())) {
            // 开启lombok的链式模式
            entityBuilder.enableChainModel();
        }
        if (BooleanUtil.isTrue(cgBO.getLombokModel())) {
            // 开启lombok模型
            entityBuilder.enableLombok();
        }
        if (BooleanUtil.isTrue(cgBO.getColumnConstant())) {
            // 开启生成字段常量
            entityBuilder.enableColumnConstant();
        }
        if (BooleanUtil.isTrue(cgBO.getFieldAnnotation())) {
            // 开启字段注解
            entityBuilder.enableTableFieldAnnotation();
        }

        Mapper.Builder mapperBuilder = builder.mapperBuilder();
        if (BooleanUtil.isTrue(cgBO.getBaseResultMap())) {
            // 开启baseResultMap
            mapperBuilder.enableBaseResultMap();
        }
        if (BooleanUtil.isTrue(cgBO.getBaseColumnList())) {
            // 开启baseColumnList
            mapperBuilder.enableBaseColumnList();
        }
        if (BooleanUtil.isTrue(cgBO.getMapperAnnotation())) {
            // 开启mapper注解
            mapperBuilder.enableMapperAnnotation();
        }

        Controller.Builder controllerBuilder = builder.controllerBuilder();
        if (BooleanUtil.isTrue(cgBO.getRestStyle())) {
            // 开启rest风格
            controllerBuilder.enableRestStyle();
        }
        if (BooleanUtil.isTrue(cgBO.getHyphenStyle())) {
            // 开启驼峰转连字符
            controllerBuilder.enableHyphenStyle();
        }
    }

    private void packageConfigBuilder(PackageConfig.Builder builder) {
        builder
                .parent(cgBO.getPackageName())
                .controller(cgBO.getPackageController())
                .entity(cgBO.getPackageEntity())
                .mapper(cgBO.getPackageMapper())
                .xml(cgBO.getPackageMapperXml())
                .service(cgBO.getPackageService())
                .serviceImpl(cgBO.getPackageServiceImpl());
    }

    private void globalConfigBuilder(GlobalConfig.Builder builder) {
        builder.author(cgBO.getAuthor());

        if (StrUtil.isNotEmpty(cgBO.getOutDir())) {
            outDir = cgBO.getOutDir();
        }
        builder.outputDir(outDir);

        // LocalDateTime
        DateType dateType = DateType.TIME_PACK;
        if ("java.util".equalsIgnoreCase(cgBO.getDateTimeType())) {
            // Date
            dateType = DateType.ONLY_DATE;
        }
        builder.dateType(dateType);

        if (BooleanUtil.isTrue(cgBO.getSwaggerSupport())) {
            builder.enableSwagger();
        }
    }

    public DataSourceConfig.Builder dataSourceBuilder() {
        return new DataSourceConfig.Builder(cgBO.getDbUrl(), cgBO.getUsername(), cgBO.getPassword());
    }
}
