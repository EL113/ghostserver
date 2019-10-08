<template>
    <section id="category" class="top-category-widget-area pt-50">
        <div class="container">
            <div class="content-details">
                <h2 class="content-title mx-auto text-uppercase mb-20">分类</h2>
            </div>
            <div class="row">
                <div v-for="item in category" :key="item.index" class="col-lg-4 mt-20">
                    <div class="single-cat-widget content relative">
                        <div class="overlay overlay-bg"></div>
                        <router-link :to="{ path: '/list', query: { category: item.title} }">
                            <div class="thumb">
                                <img class="content-image img-fluid d-block mx-auto" src="/static/img/blog/cat-widget1.jpg" alt="">
                            </div>
                            <div class="content-details">
                                <h4 class="content-title mx-auto text-uppercase">{{item.title}}</h4>
                            </div>
                        </router-link>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
export default {
  name: 'Category',
  data () {
    return {
      category: [{title: 'category0', id: 0}, {title: 'category0', id: 1}, {title: 'category0', id: 2}, {title: 'category0', id: 3}, {title: 'category0', id: 4}, {title: 'category0', id: 5}]
    }
  }
}
</script>

<style scoped>

</style>
