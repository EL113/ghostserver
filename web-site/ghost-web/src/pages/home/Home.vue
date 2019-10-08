<template>
    <div>
        <Header></Header>
        <Banner></Banner>
        <Category></Category>
        <Recommend></Recommend>
    </div>
</template>

<script>
import Header from './components/Header'
import Banner from './components/Banner'
import Category from './components/Category'
import Recommend from './components/Recommend'

export default {
  name: 'Home',
  components: {
    Header,
    Banner,
    Category,
    Recommend
  }
}
</script>

<style scoped>

</style>
