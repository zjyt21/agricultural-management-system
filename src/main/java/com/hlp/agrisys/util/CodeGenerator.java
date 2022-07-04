package com.hlp.agrisys.util;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * MyBatis-Plus code generator
 *
 * @author Mr.Han
 * @create 2022-06-23 22:23
 */
public class CodeGenerator {

    /**
     * <p>
     * read console content
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("please input" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("Please enter the correct" + tip + "！");
    }

    public static void main(String[] args) {
        // code generatorr
        AutoGenerator mpg = new AutoGenerator();

        // global confiure
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("hlp");
        gc.setOpen(false);
        gc.setSwagger2(true); //Entity Properties Swagger2 Annotations
        gc.setServiceName("I%sService");
        mpg.setGlobalConfig(gc);

        // datasource config
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/smart_farming");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("hlproot");
        mpg.setDataSource(dsc);

        // package config
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null);//if there is not, fill in null
        pc.setParent("com.hlp.agrisys");
        mpg.setPackageInfo(pc);

        // custom configuration
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // If the template engine is freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // If the template engine is velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // Custom output configuration
        List<FileOutConfig> focList = new ArrayList<>();
        // Custom configuration will be output first
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // Customize the output file name, if your Entity has set the prefix and suffix, note that the name of the xml will change accordingly! !
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // configuration template
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // Policy configuration
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("Table name, separated by multiple commas").split(","));
        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix("sys_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}