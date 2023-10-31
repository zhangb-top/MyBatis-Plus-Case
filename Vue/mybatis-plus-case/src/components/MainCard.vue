<template>
  <div class="main-card-container">
    <!-- 配置项 -->
    <div class="config-container">
      <!-- 左侧配置项 -->
      <div class="left">
        <!-- 项目 -->
        <form-item name="项目" :isRequired="true" :form="forms.projectForm" :rules="rules.projectRules"></form-item>
        <!-- 数据库配置 -->
        <form-item name="数据库配置" :isRequired="true" :form="forms.dbConfigForm" :rules="rules.dbConfigRules" v-on:selectDB="selectDB">
          <el-button type="primary" icon="el-icon-edit" @click="dialogFormVisible=true"></el-button>
        </form-item>
        <el-dialog title="数据库配置" :visible.sync="dialogFormVisible" width="50%">
          <el-form ref="form" :model="forms.dbForm" :rules="rules.dbRules" label-width="80px">
            <el-form-item>
              <el-select style="margin-right:12px" v-model="forms.dbForm.db" placeholder="添加新的配置" clearable @change="selectDB">
                <el-option v-for="(item,index) in dbConfigList" :key="index" :label="item.db" :value="item.db"></el-option>
              </el-select>
              <el-button icon="el-icon-plus" @click="addDBConfig"></el-button>
              <el-button type="danger" icon="el-icon-delete" @click="removeDBConfig"></el-button>
            </el-form-item>
            <el-form-item label="连接名" prop="db">
              <el-input v-model="forms.dbForm.db" placeholder="请输入连接名"></el-input>
            </el-form-item>
            <el-form-item label="类型" prop="dbTypeName">
              <el-select v-model="forms.dbForm.dbTypeName" placeholder="请选择数据库类型" clearable>
                <el-option v-for="(item,index) in dbTypeNameList" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="地址" prop="host">
              <el-input v-model="forms.dbForm.host" placeholder="请输入数据库主机或IP地址"></el-input>
            </el-form-item>
            <el-form-item label="用户名" prop="username">
              <el-input v-model="forms.dbForm.username" placeholder="请输入数据库用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="forms.dbForm.password" placeholder="请输入数据库密码"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveDBConfig('form')">确 定</el-button>
          </div>
        </el-dialog>
        <!-- 可选配置 -->
        <form-item name="可选配置" :isRequired="false" :form="forms.optionalForm"></form-item>
        <!-- 表配置 -->
        <form-item name="表配置" :isRequired="true" :form="forms.tableOptionsForm" :rules="rules.tableOptionsRules"></form-item>
      </div>
      <!-- 中间配置项 -->
      <div class="center">
        <!-- 包配置 -->
        <form-item name="包配置" :isRequired="true" :form="forms.pageOptionsForm" :rules="rules.pageOptionsRules"></form-item>
        <!-- 文件名格式 -->
        <form-item name="文件名格式" :isRequired="true" :form="forms.fileNameFormatForm" :rules="rules.fileNameFormatRules"></form-item>
      </div>
      <!-- 右侧配置项 -->
      <div class="right">
        <!-- 字段名配置 -->
        <form-item name="字段名配置" :form="forms.fieldNameConfigForm"></form-item>
        <!-- 实体类配置 -->
        <form-item name="实体类配置" :form="forms.entityConfigForm"></form-item>
        <!-- XML配置 -->
        <form-item name="XML配置" :form="forms.XMLConfigForm"></form-item>
        <!-- 其他配置 -->
        <form-item name="其他配置" :form="forms.otherConfigForm"></form-item>
      </div>
    </div>
    <!-- 底部按钮 -->
    <div class="footer-container">
      <el-row>
        <el-button type="success" @click="codeGenerator">立即生成</el-button>
        <el-button type="primary" @click="saveConfig(true)">保存配置</el-button>
        <el-button type="danger" @click="clearConfig">清除配置</el-button>
      </el-row>
    </div>
  </div>
</template>

<script>
import { codeGeneratorAPI } from '@/api/indexAPI'
import FormItem from './FormItem.vue'

export default {
  components: { FormItem },
  mounted() {
    // 设置isAddDBConfig

    // 加载dbConfigList
    this.loadDBOptions()
    this.forms.dbConfigForm.db.dbConfigList = this.dbConfigList
    // 加载codeGeneratorDTO
    this.loadCodeGeneratorDTO()
  },
  data() {
    return {
      codeGeneratorDTO: {},
      forms: {
        // 项目表单
        projectForm: {
          packageName: {
            content: '',
            chinese: '包名'
          },
          superClassName: {
            content: '',
            chinese: '实体父类'
          }
        },
        // 数据库配置表单
        dbConfigForm: {
          db: {
            content: '',
            chinese: '数据库',
            type: 'select',
            dbConfigList: [],
            hasSlot: true
          },
          dbName: {
            content: '',
            chinese: '数据库名称'
          }
        },
        // 数据库表单
        dbForm: {
          db: '',
          dbTypeName: '',
          host: '',
          username: '',
          password: ''
        },
        // 可选配置表单
        optionalForm: {
          author: {
            content: '',
            chinese: '作者'
          },
          outDir: {
            content: '',
            chinese: '输出目录'
          }
        },
        // 表配置表单
        tableOptionsForm: {
          ignoreColumns: {
            content: '',
            chinese: '忽略字段',
            type: 'textarea'
          },
          tableNames: {
            content: '',
            chinese: '表名',
            type: 'textarea'
          },
          fieldPrefixes: {
            content: '',
            chinese: '字段前缀'
          },
          excludeTableNames: {
            content: '',
            chinese: '排除的表名',
            type: 'textarea'
          }
        },
        // 包配置表单
        pageOptionsForm: {
          packageEntity: {
            content: 'entity',
            chinese: '实体类'
          },
          packageMapper: {
            content: 'mapper',
            chinese: 'mapper'
          },
          packageMapperXml: {
            content: 'mapper',
            chinese: 'mapper xml'
          },
          packageService: {
            content: 'service',
            chinese: 'service'
          },
          packageServiceImpl: {
            content: 'service.impl',
            chinese: 'service.impl'
          },
          packageController: {
            content: 'controller',
            chinese: 'controller'
          }
        },
        // 文件名格式表单
        fileNameFormatForm: {
          fileNamePatternEntity: {
            content: '%sEntity',
            chinese: '实体类'
          },
          fileNamePatternMapper: {
            content: '%sMapper',
            chinese: 'mapper'
          },
          fileNamePatternMapperXml: {
            content: '%sMapper',
            chinese: 'mapper xml'
          },
          fileNamePatternService: {
            content: '%sService',
            chinese: 'service'
          },
          fileNamePatternServiceImpl: {
            content: '%sServiceImpl',
            chinese: 'service.impl'
          },
          fileNamePatternController: {
            content: '%sController',
            chinese: 'controller'
          }
        },
        // 字段名配置表单
        fieldNameConfigForm: {
          fieldLogicDelete: {
            content: '',
            chinese: '逻辑删除'
          },
          fieldVersion: {
            content: '',
            chinese: '乐观锁'
          }
        },
        // 实体类配置表单
        entityConfigForm: {
          swaggerSupport: {
            content: false,
            chinese: 'Swagger',
            type: 'switch'
          },
          dateTimeType: {
            content: 'java.time.LocalDateTime',
            chinese: '日期类型',
            type: 'radio-group',
            radios: ['java.util.Date', 'java.time.LocalDateTime']
          },
          lombokModel: {
            content: true,
            chinese: 'lombok',
            type: 'switch'
          },
          lombokChainModel: {
            content: true,
            chinese: '链式',
            type: 'switch'
          },
          fieldAnnotation: {
            content: true,
            chinese: '字段注解',
            type: 'switch'
          },
          columnConstant: {
            content: false,
            chinese: '列常量',
            type: 'switch'
          }
        },
        // XML配置表单
        XMLConfigForm: {
          baseResultMap: {
            content: false,
            chinese: '基础结果Map',
            type: 'switch'
          },
          baseColumnList: {
            content: false,
            chinese: '基础列名List',
            type: 'switch'
          }
        },
        // 其他配置
        otherConfigForm: {
          mapperAnnotation: {
            content: true,
            chinese: '@Mapper',
            type: 'switch'
          }
        }
      },
      rules: {
        // 项目表单验证规则
        projectRules: {
          packageName: [{ required: true, message: '请输入包名', trigger: 'blur' }]
        },
        // 数据库配置表单规则
        dbConfigRules: {
          db: [{ required: true, message: '请选择数据库', trigger: 'blur' }],
          dbName: [{ required: true, message: '请输入数据库名称', trigger: 'blur' }]
        },
        // 数据库表单规则
        dbRules: {
          db: [{ required: true, message: '请输入连接名', trigger: 'blur' }],
          dbTypeName: [{ required: true, message: '请选择数据库类型', trigger: 'blur' }],
          host: [{ required: true, message: '请输入地址', trigger: 'blur' }],
          username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
          password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
        },
        // 表配置表单规则
        tableOptionsRules: {
          tableNames: [{ required: true, message: '请输入表名', trigger: 'blur' }]
        },
        // 包配置表单规则
        pageOptionsRules: {
          packageEntity: [{ required: true, message: '请输入实体类包名', trigger: 'blur' }],
          packageMapper: [{ required: true, message: '请输入mapper包名', trigger: 'blur' }],
          packageMapperXml: [{ required: true, message: '请输入mapper xml包名', trigger: 'blur' }],
          packageService: [{ required: true, message: '请输入service包名', trigger: 'blur' }],
          packageServiceImpl: [{ required: true, message: '请输入service.impl包名', trigger: 'blur' }],
          packageController: [{ required: true, message: '请输入controller包名', trigger: 'blur' }]
        },
        // 文件名格式表单规则
        fileNameFormatRules: {
          fileNamePatternEntity: [{ required: true, message: '请输入实体类文件名格式', trigger: 'blur' }],
          fileNamePatternMapper: [{ required: true, message: '请输入mapper文件名格式', trigger: 'blur' }],
          fileNamePatternMapperXml: [{ required: true, message: '请输入mapper xml文件名格式', trigger: 'blur' }],
          fileNamePatternService: [{ required: true, message: '请输入service文件名格式', trigger: 'blur' }],
          fileNamePatternServiceImpl: [{ required: true, message: '请输入service.impl文件名格式', trigger: 'blur' }],
          fileNamePatternController: [{ required: true, message: '请输入controller文件名格式', trigger: 'blur' }]
        }
      },
      // 是否展示数据库表单
      dialogFormVisible: false,
      // 是否是新增数据库配置
      isAddDBConfig: true,
      // 当前数据库配置的选择项
      currentDBConfig: '',
      // 保存在本地的数据库配置
      dbConfigList: [],
      // 数据库可选类型
      dbTypeNameList: ['MySQL', 'Oracle'],
      keys: {
        dbListKey: 'MPCASE-DB-LIST',
        configKey: 'MPCASE-CONFIG'
      }
    }
  },
  methods: {
    // 设置isAddDBConfig
    setIsAddDBConfig() {
      if (!(localStorage.getItem(this.keys.configKey) === '{}' || !localStorage.getItem(this.keys.configKey))) {
        this.isAddDBConfig = false
      }
    },
    // 请求后端接口
    async codeGenerator() {
      this.prepareConfig()
      // 判断表单整体是否填写正确
      if (!this.isFormCorrect()) return this.$message.error('表单必填项未填写')
      const { data: res } = await codeGeneratorAPI(this.codeGeneratorDTO)
      if (res.success) return this.$message.success('生成成功')
      this.$message.error('生成失败')
    },
    // 判断表单整体是否填写正确
    isFormCorrect() {
      for (const key in this.rules) {
        const rules = this.rules[key]
        for (const key2 in rules) {
          if (this.codeGeneratorDTO[key2] === '') return false
        }
      }
      return true
    },
    // 准备全部配置
    prepareConfig() {
      for (const key in this.forms) {
        const form = this.forms[key]
        for (const key2 in form) {
          if (key === 'dbForm') {
            this.codeGeneratorDTO[key2] = form[key2]
          } else {
            this.codeGeneratorDTO[key2] = form[key2].content
          }
        }
      }
    },
    // 加载this.codeGeneratorDTO
    loadCodeGeneratorDTO() {
      if (localStorage.getItem(this.keys.configKey) === '{}' || !localStorage.getItem(this.keys.configKey)) return
      this.codeGeneratorDTO = localStorage.getItem(this.keys.configKey) ? JSON.parse(localStorage.getItem(this.keys.configKey)) : {}
      for (const key in this.forms) {
        const form = this.forms[key]
        for (const key2 in form) {
          if (key === 'dbForm') {
            form[key2] = this.codeGeneratorDTO[key2]
          } else {
            form[key2].content = this.codeGeneratorDTO[key2]
          }
        }
      }
      // 设置this.currentDBConfig
      this.setCurrentDBConfig(this.forms.dbForm)
    },
    // 保存全部配置
    saveConfig(flag) {
      this.prepareConfig()
      localStorage.setItem(this.keys.configKey, JSON.stringify(this.codeGeneratorDTO))
      if (flag) this.$message.success('成功保存全局配置')
    },
    // 清空配置
    clearConfig() {
      localStorage.clear()
      this.$message.success('成功清空全部配置')
    },
    // 保存数据库配置
    saveDBConfig(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (!this.isAddDBConfig) {
            this.forms.dbConfigForm.db.content = this.forms.dbForm.db
            this.dialogFormVisible = false
            return
          }
          let flag = true
          for (let i = 0; i < this.dbConfigList.length; i++) {
            // 重名
            if (this.dbConfigList[i].db === this.forms.dbForm.db) {
              this.dbConfigList[i] = this.forms.dbForm
              flag = false
            }
          }
          if (flag) this.dbConfigList.push(this.forms.dbForm)
          localStorage.setItem(this.keys.dbListKey, JSON.stringify(this.dbConfigList))
          this.forms.dbConfigForm.db.content = this.forms.dbForm.db
          // 关闭弹出框
          this.dialogFormVisible = false
          // 重新加载dbConfigList
          this.loadDBOptions()
          // 设置currentDBConfig
          this.setCurrentDBConfig(this.forms.dbForm)
          // 保存全局配置
          if (!flag) this.saveConfig(flag)
        } else {
          this.$message.error('请输入正确的表单信息')
          return false
        }
      })
    },
    // 新增数据库配置
    addDBConfig() {
      this.isAddDBConfig = true
      this.clearDBFrom()
      this.loadDBOptions()
    },
    // 清空this.forms.dbForm的内容
    clearDBFrom() {
      for (const key in this.forms.dbForm) {
        this.forms.dbForm[key] = ''
      }
    },
    // 清空当前的数据库配置
    removeDBConfig() {
      this.dbConfigList.forEach((item, index) => {
        if (JSON.stringify(item) === this.currentDBConfig) {
          this.dbConfigList.splice(index, 1)
          localStorage.setItem(this.keys.dbListKey, JSON.stringify(this.dbConfigList))
          this.clearDBFrom()
          this.forms.dbConfigForm.db.content = ''
          this.currentDBConfig = ''
          this.loadDBOptions()
        }
      })
      // 保存全局配置
      this.saveConfig(false)
      this.$message.success('成功清除该数据库配置')
    },
    // 加载dbOptions
    loadDBOptions() {
      this.dbConfigList = localStorage.getItem(this.keys.dbListKey) ? JSON.parse(localStorage.getItem(this.keys.dbListKey)) : []
    },
    // 选择数据库配置
    selectDB(value) {
      if (value === '') {
        this.forms.dbConfigForm.db.content = value
        this.clearDBFrom()
        this.isAddDBConfig = true
        return
      }
      this.isAddDBConfig = false
      const current = this.dbConfigList.filter(item => item.db === value)
      this.forms.dbConfigForm.db.content = value
      this.forms.dbForm = current[0]
      this.loadDBOptions()
      this.setCurrentDBConfig(current[0])
      this.forms.dbConfigForm.db.dbConfigList = this.dbConfigList
    },
    // 设置this.currentDBConfig
    setCurrentDBConfig(value) {
      this.currentDBConfig = JSON.stringify(value)
    }
  }
}
</script>

<style lang="scss" scope>
.main-card-container {
  width: 90%;
  border-radius: 0.625rem;
  padding: 1.25rem;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: auto;
  background-color: rgba(255, 255, 255, 0.8);
  .config-container {
    display: flex;
    justify-content: space-between;
    .left,
    .center,
    .right {
      width: 30%;
    }
  }
  .footer-container {
    text-align: center;
  }
}
</style>