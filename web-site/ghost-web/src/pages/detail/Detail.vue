<template>
<div>
  <Header/>
  <section class="post-content-area single-post-area">
    <div class="container">
      <div class="row">
        <Content :story_item='story_item'/>
        <SideArea/>
      </div>
    </div>
  </section>
  <Footer/>
</div>
</template>

<script>
import Header from '../home/components/Header'
import Footer from '../home/components/Footer'
import Content from './components/Content'
import SideArea from './components/SideArea'

export default {
  name: 'Detail',
  components: {
    Content,
    SideArea,
    Header,
    Footer
  },
  data () {
    return {
      story_item: this.$route.query.story_item
    }
  }
}
</script>
