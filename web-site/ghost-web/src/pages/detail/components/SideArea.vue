<template>
  <div class="col-lg-4 sidebar-widgets widget-wrap">
    <div class="single-sidebar-widget search-widget">
      <form class="search-form" action="#">
          <input placeholder="搜索故事" name="search" type="text"
            onfocus="this.placeholder = ''"
            onblur="this.placeholder = '搜索故事'"
            v-model="keyword">
          <button type="submit" @click="onSearch()"><font-awesome-icon icon="search"/></button>
      </form>
    </div>
    <div class="single-sidebar-widget post-category-widget">
      <h4 class="category-title">Catgories</h4>
      <ul class="cat-list">
        <li v-for="catItem in catList" :key="catItem.index">
          <a href="#" class="d-flex justify-content-between">
            <p>{{catItem.name}}</p>
            <p>{{catItem.count}}</p>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
// import axios from 'axios'

export default {
  name: 'SideArea',
  data () {
    return {
      catList: [
        {
          name: '校园',
          code: 'xy',
          count: 0
        },
        {
          name: '原创',
          code: 'yc',
          count: 0
        }
      ],
      keyword: ''
    }
  },
  methods: {
    // requestCatList () {
    //   axios.get('/catList').then(this.responseData)
    // },
    // responseData (response) {
    //   if (response.status !== 200) {
    //     console.log('请求失败')
    //     return
    //   }
    //   this.catList = response.data.result
    // },
    onSearch () {
      this.$router.push({
        path: 'storyList',
        query: {
          keyword: this.keyword
        }
      })
    }
  },
  mounted () {
    // this.requestCatList()
  }
}
</script>
