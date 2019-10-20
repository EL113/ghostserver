<template>
  <!-- article content -->
  <div class="col-lg-7 offset-lg-1 posts-list single-post row">
    <div class="col-lg-11 col-md-11">
      <h1 class="mt-20 mb-20">{{story_item.title}}</h1>
      <h3 class="mt-20 mb-20">{{story_item.author}}</h3>
      <p class="excert">
        {{content}}
      </p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Content',
  props: {
    story_item: Object
  },
  data () {
    return {
      content: '',
      pageNo: 1,
      pageSize: 10
    }
  },
  mounted () {
    this.requestDetail()
  },
  methods: {
    requestDetail () {
      axios.get('/openapi/content', {
        params: {
          id: this.story_item.id,
          type: this.story_item.type,
          pageNo: this.pageNo,
          pageSize: this.pageSize
        }
      }).then(this.detailResponse)
    },
    detailResponse (response) {
      if (response.status !== 200) {
        console.log('detail request error')
        return
      }
      var result = response.data.result
      this.title = result.title
      this.author = result.author
      this.content = result.content
    }
  }
}
</script>
