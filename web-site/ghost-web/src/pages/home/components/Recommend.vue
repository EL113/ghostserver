<template>
  <section id="recommend" class="post-content-area pt-50">
    <div class="container">
      <h2 class="content-title mx-auto text-uppercase mb-20 mt-40">推荐</h2>
      <div class="row">
        <!-- 推荐列表 -->
        <div class="col-lg-8 offset-lg-3 posts-list">
          <div v-for="item in list" :key="item.id" class="single-post row">
            <div class="col-lg-9 col-md-9">
              <router-link :to="{ path: 'detail', query: { type: item.type, id: item.id} }" class="posts-title">
                <h3>{{item.title}}</h3>
              </router-link>
              <p class="excert">
                {{item.brief}}
              </p>
            </div>
          </div>

          <nav class="blog-pagination justify-content-center d-flex">
            <ul class="pagination">
              <li class="page-item page-link" @click="firstPage">首页</li>
              <li class="page-item page-link">
                <font-awesome-icon icon="chevron-left" @click="prePage"/>
              </li>
              <li v-for="pageItem in pageList" :key="pageItem" @click="toPage(pageItem)">
                <div v-if="pageItem === pageNo" class="page-item page-link" style="background-color:#8490ff;color:#fff">
                  {{pageItem}}
                </div>
                <div v-else class="page-item page-link">
                  {{pageItem}}
                </div>
              </li>
              <li class="page-item page-link">
                <font-awesome-icon icon="chevron-right" @click="nextPage"/>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios'
import { range } from '@/utils/util.js'

export default {
  name: 'Recommend',
  data () {
    return {
      pageNo: 1,
      pageList: [ 1, 2, 3, 4 ],
      maxPageNo: 0,
      list: []
    }
  },
  methods: {
    requestHomeInfo () {
      axios.get('/openapi/recommend', {
        params: {
          pageNo: this.pageNo,
          pageSize: 2
        }
      }).then(this.homeInfoHandle)
    },
    homeInfoHandle (data) {
      if (data.status !== 200) {
        return
      }
      this.list = data.data.result.list
      this.maxPageNo = data.data.result.maxPageNo
      if (this.maxPageNo < 5) {
        this.pageList = range(1, this.maxPageNo, 1)
      } else if (this.pageNo <= 3) {
        this.pageList = range(1, 5, 1)
      } else if (this.pageNo > 3 && this.pageNo <= this.maxPageNo - 5) {
        this.pageList = range(this.pageNo - 2, this.pageNo + 2, 1)
      } else if (this.pageNo > this.maxPageNo - 5) {
        this.pageList = range(this.maxPageNo - 4, this.maxPageNo, 1)
      }
      console.log(this.pageList)
    },
    firstPage () {
      this.pageNo = 1
    },
    prePage () {
      if (this.pageNo > 1) {
        this.pageNo--
      }
    },
    nextPage () {
      if (this.pageNo < this.maxPageNo) {
        this.pageNo++
      }
    },
    toPage (pageNo) {
      this.pageNo = pageNo
    }
  },
  watch: {
    pageNo () {
      this.requestHomeInfo()
    }
  },
  mounted () {
    this.requestHomeInfo()
  }
}
</script>

<style scoped>

</style>
