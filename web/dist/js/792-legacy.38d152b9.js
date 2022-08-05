"use strict";(self["webpackChunkfarmer_center"]=self["webpackChunkfarmer_center"]||[]).push([[792],{82792:function(e,t,l){l.r(t),l.d(t,{default:function(){return c}});var a=function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("div",[l("div",{staticStyle:{margin:"10px 0"}},[l("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"Please input username","suffix-icon":"el-icon-search"},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),l("el-input",{staticClass:"ml-5",staticStyle:{width:"200px"},attrs:{placeholder:"Please input nickname","suffix-icon":"el-icon-search"},model:{value:e.nickname,callback:function(t){e.nickname=t},expression:"nickname"}}),l("el-input",{staticClass:"ml-5",staticStyle:{width:"200px"},attrs:{placeholder:"Please input email","suffix-icon":"el-icon-message"},model:{value:e.email,callback:function(t){e.email=t},expression:"email"}}),l("el-button",{staticClass:"ml-5",attrs:{type:"primary"},on:{click:e.loadPage}},[e._v("Search")]),l("el-button",{staticClass:"ml-5",attrs:{type:"warning"},on:{click:e.reset}},[e._v("Reset")])],1),l("div",{staticStyle:{margin:"10px 0"}},[l("el-button",{attrs:{type:"primary"},on:{click:e.handleAdd}},[e._v("Add "),l("i",{staticClass:"el-icon-circle-plus-outline"})]),l("el-popconfirm",{staticClass:"ml-5",attrs:{"confirm-button-text":"DELETE","cancel-button-text":"CANCEL",icon:"el-icon-info","icon-color":"red",title:"Are you sure you want to delete these data?"},on:{cancel:e.cancel,confirm:e.delBatch}},[l("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("Delete in bulk "),l("i",{staticClass:"el-icon-remove-outline"})])],1)],1),l("el-table",{attrs:{"default-sort":{prop:"id",order:"ascending"},data:e.tableData,border:"","header-cell-style":{textAlign:"center",background:"beige"},stripe:"","cell-style":e.rowClass},on:{"selection-change":e.handleSelectionChange}},[l("div",{staticStyle:{"text-align":"left"},attrs:{slot:"empty"},slot:"empty"},[l("el-empty",{attrs:{description:"Sorry, there is no data you are looking for"}})],1),l("el-table-column",{attrs:{type:"selection",width:"55"}}),l("el-table-column",{attrs:{prop:"id",label:"ID",sortable:"",width:"80"}}),l("el-table-column",{attrs:{prop:"username",label:"Username",width:"140"}}),l("el-table-column",{attrs:{prop:"nickname",label:"Nickname",width:"120"}}),l("el-table-column",{attrs:{prop:"role",label:"Role",width:"200"}}),l("el-table-column",{attrs:{prop:"email",label:"Email"}}),l("el-table-column",{attrs:{prop:"phonenumber",label:"Phone"}}),l("el-table-column",{attrs:{prop:"option",label:"Operation"},scopedSlots:e._u([{key:"default",fn:function(t){return[l("el-button",{attrs:{type:"success"},on:{click:function(l){return e.handleEdit(t.row)}}},[e._v("Edit "),l("i",{staticClass:"el-icon-edit"})]),l("el-popconfirm",{staticClass:"ml-5",attrs:{"confirm-button-text":"DELETE","cancel-button-text":"CANCEL",icon:"el-icon-info","icon-color":"red",title:"Are you sure to delete?"},on:{confirm:function(l){return e.del(t.row.id)},cancel:e.cancel}},[l("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("Delete "),l("i",{staticClass:"el-icon-remove-outline"})])],1)]}}])})],1),l("div",{staticStyle:{padding:"10px 0"}},[l("el-pagination",{attrs:{"current-page":e.pageNum,"page-sizes":[5,10,20],"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),l("el-dialog",{attrs:{title:"User Info",visible:e.dialogFormVisible,width:"30%"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[l("el-form",{attrs:{"label-width":"100px",size:"small"}},[l("el-form-item",{attrs:{label:"Username"}},[l("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.username,callback:function(t){e.$set(e.form,"username",t)},expression:"form.username"}})],1),l("el-form-item",{attrs:{label:"Nickname"}},[l("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.nickname,callback:function(t){e.$set(e.form,"nickname",t)},expression:"form.nickname"}})],1),l("el-form-item",{attrs:{label:"Email"}},[l("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.email,callback:function(t){e.$set(e.form,"email",t)},expression:"form.email"}})],1),l("el-form-item",{attrs:{label:"Phone"}},[l("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.phonenumber,callback:function(t){e.$set(e.form,"phonenumber",t)},expression:"form.phonenumber"}})],1),l("el-form-item",{attrs:{label:"Role"}},[l("el-select",{staticStyle:{width:"100%"},attrs:{clearable:""},model:{value:e.form.role,callback:function(t){e.$set(e.form,"role",t)},expression:"form.role"}},e._l(e.roles,(function(e){return l("el-option",{key:e.roleKey,attrs:{label:e.roleKey,value:e.roleKey}})})),1)],1)],1),l("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[l("el-button",{on:{click:e.formCancel}},[e._v("Cancel")]),l("el-button",{attrs:{type:"primary"},on:{click:e.save}},[e._v("Save")])],1)],1)],1)},n=[],i=(l(21249),{name:"User",data:function(){return{tableData:[],total:0,pageNum:1,pageSize:5,username:"",nickname:"",email:"",form:{},multipleSelection:[],dialogFormVisible:!1,roles:[]}},created:function(){this.loadPage()},methods:{rowClass:function(){return"text-align:center"},cancel:function(){this.$message.warning("Operation successfully canceled")},formCancel:function(){this.dialogFormVisible=!1,this.$message.warning("Form submission canceled")},loadPage:function(){var e=this;this.request.get("/user/page",{params:{currentPage:this.pageNum,pageSize:this.pageSize,username:this.username,email:this.email,nickname:this.nickname}}).then((function(t){console.log(t),e.tableData=t.data.records,e.total=t.data.total})),this.request.get("/role").then((function(t){e.roles=t.data}))},handleSizeChange:function(e){this.pageSize=e,this.loadPage()},handleCurrentChange:function(e){this.pageNum=e,this.loadPage()},reset:function(){this.username="",this.email="",this.nickname="",this.loadPage()},handleAdd:function(){this.dialogFormVisible=!0,this.form={}},save:function(){var e=this;this.request.post("/user",this.form).then((function(t){200==t.code?(e.$message.success("Successfully saved"),e.dialogFormVisible=!1,e.loadPage()):e.$message.error("Failed to save, please try again")}))},handleEdit:function(e){this.form=Object.assign({},e),this.dialogFormVisible=!0},del:function(e){var t=this;this.request.delete("/user/"+e).then((function(e){200==e.code?(t.$message.success("Successfully deleted"),t.loadPage()):t.$message.error("Failed to delete")}))},handleSelectionChange:function(e){this.multipleSelection=e},delBatch:function(){var e=this,t=this.multipleSelection.map((function(e){return e.id}));this.request.post("/user/batchDel",t).then((function(t){200==t.code?(e.$message.success("Successfully deleted"),e.loadPage()):e.$message.error("Failed to delete")}))}}}),o=i,s=l(1001),r=(0,s.Z)(o,a,n,!1,null,null,null),c=r.exports}}]);
//# sourceMappingURL=792-legacy.38d152b9.js.map