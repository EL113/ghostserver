<template>
    <div>
        <Header></Header>
        <Banner></Banner>
        <Category></Category>
        <Recommend :list="recommend"></Recommend>
        <AppDownload></AppDownload>
        <Footer></Footer>
    </div>
</template>

<script>
import Header from './components/Header'
import Banner from './components/Banner'
import Category from './components/Category'
import Recommend from './components/Recommend'
import AppDownload from './components/AppDownload'
import Footer from './components/Footer'
import axios from 'axios'

export default {
  name: 'Home',
  components: {
    Header,
    Banner,
    Category,
    Recommend,
    AppDownload,
    Footer
  },
  methods: {
    requestHomeInfo () {
      axios.get('/openapi/recommend?pageNo=1&pageSize=15', {
        pageNo: 0,
        pageSize: 25
      }).then(this.homeInfoHandle)
    },
    homeInfoHandle (data) {
      if (data.status !== 200) {
        return
      }

      console.log('set data')
      this.recommend = data.data.result
      console.log(data.data.result)
    }
  },
  mounted () {
    this.requestHomeInfo()
  },
  // updated () {
  //   this.requestHomeInfo()
  // },
  data () {
    return {
      recommend: [
        //   {
        //   author: 'annomous',
        //   brief: '记得小时候外祖父(已去世多年)的坟被雨水冲塌了，外公张罗着迁坟，家里请来了一个阴阳先生，这个故事就是阴阳先生和我说的。他有一次也是去迁坟，死了的是一个女人，这家人的儿媳妇。她刚生下一个儿子，上一刻还和别...',
        //   id: 3,
        //   title: '阴阳先生的故事',
        //   type: 'dp'
        // }
      ]
    }
  }
}
</script>

<style scoped>

</style>
