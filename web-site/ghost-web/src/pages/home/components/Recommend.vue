<template>
    <section id="recommend" class="post-content-area pt-50">
        <div class="container">
            <div class="content-details mb-20">
                <h2 class="content-title mx-auto text-uppercase mb-20">推荐</h2>
            </div>
            <div class="row">
                <div class="col-lg-8 offset-lg-3 posts-list">
                    <div v-for="item in result.list" :key="item.id" class="single-post row">
                        <div class="col-lg-9 col-md-9">
                            <router-link :to="{ path: 'detail', query: { type: item.type, id: item.id} }" class="posts-title">
                                <h3>{{item.title}}</h3>
                            </router-link>
                            <p class="excert">
                                {{item.desc}}
                            </p>
                        </div>
                    </div>

                    <nav class="blog-pagination justify-content-center d-flex">
                        <ul class="pagination">
                            <li class="page-item page-link" @click="firstPage">首页</li>
                            <li class="page-item page-link">
                                <font-awesome-icon icon="chevron-left" @click="prePage"/>
                            </li>
                            <li v-for="pageItem in result.pageList" :key="pageItem" class="page-item page-link" @click="toPage">
                                {{pageItem}}
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
export default {
  name: 'Recommend',
  data () {
    return {
      result: {
        list:
        [
          {
            title: 'title1',
            desc: 'desc',
            id: 0,
            type: 'xy'
          }
        ],
        pageList:
        [
          1, 2, 3, '...', 15, 16, 17
        ]
      }
    }
  },
  methods: {
    firstPage () {
      return 0
    },
    prePage () {
      return 0
    },
    nextPage () {
      return 0
    },
    toPage () {
      return 0
    }
  }
}
</script>

<style scoped>

</style>
