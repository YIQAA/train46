import { createStore } from 'vuex'

const MEMBER = "MEMBER"

export default createStore({
  state: {
    member: window.sessionStorage.getItem(MEMBER) || {}
  },
  getters: {
  },
  mutations: {
    setMember (state, member) {
      state.member = member;
      window.sessionStorage.setItem(MEMBER, member);
    }
  },
  actions: {
  },
  modules: {
  }
})
