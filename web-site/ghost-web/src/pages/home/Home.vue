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

export default {
  name: 'Home',
  components: {
    Header,
    Banner,
    Category,
    Recommend,
    AppDownload,
    Footer
  }
}
</script>

<style scoped>

</style>
