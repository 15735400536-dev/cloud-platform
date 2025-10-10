// 初始化root用户（兼容部分镜像的初始化问题）
db.createUser({
    user: "root",
    pwd: "MaXinHai!970923",
    roles: [{ role: "root", db: "admin" }]
});

// 验证连接
db.runCommand({ ping: 1 });