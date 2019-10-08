<template>
<div id="header">
    <div class="container main-menu">
        <div class="row align-items-center justify-content-between d-flex">
            <div id="logo">
                <a href="index.html" class="nav-brand fz-18">夜半故事</a>
            </div>
            <div class="row align-items-center">
                <nav id="nav-menu-container">
                    <ul class="nav-menu">
                        <li><a href="#home">首页</a></li>
                        <li><a href="#category">分类</a></li>
                        <li><a href="#recommend">推荐</a></li>
                        <li><a href="#project">app下载</a></li>
                    </ul>
                </nav>
                <a href="/ghostServer/edit.html" class="genric-btn primary circle ml-20">分享故事</a>
            </div>
        </div>
    </div>
</div>
</template>

<script>
export default {
  name: 'Header'
}
</script>

<style scoped>
    .main-menu {
        padding-bottom: 20px;
        padding-top: 20px;
    }

    #header {
        position: fixed;
        left: 0;
        top: 0;
        right: 0;
        transition: all 0.5s;
        z-index: 997;
        background: #fff;
    }

    #header.header-scrolled {
        transition: all 0.5s;
        box-shadow: -21.213px 21.213px 30px 0px rgba(158, 158, 158, 0.3);
    }

    #header.header-scrolled .main-menu {
        border-color: transparent;
    }

    @media (max-width: 673px) {
        #logo {
            margin-left: 20px;
        }
    }
</style>
