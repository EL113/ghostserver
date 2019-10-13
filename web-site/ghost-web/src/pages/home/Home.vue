<template>
    <div>
        <Header></Header>
        <Banner></Banner>
        <Category></Category>
        <Recommend></Recommend>
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
      axios.get('/recommend', {
        pageNo: 0,
        pageSize: 25
      }).then(this.homeInfoHandle)
    },
    homeInfoHandle (data) {

    }
  },
  mounted () {
    this.requestHomeInfo()
  }
}
</script>

<style scoped>

</style>
