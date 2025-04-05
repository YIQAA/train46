<template>
  <!-- 路由视图容器 -->
  <router-view/>
</template>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen,
  Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

html {
  font-size: 16px;
  line-height: 1.5;
}

body {
  background-color: #f8fafc;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 全局表单元素增强 */
input, button {
  transition: all 0.3s ease;
}

/* 响应式基础 */
@media (max-width: 768px) {
  html {
    font-size: 14px;
  }
}
</style>