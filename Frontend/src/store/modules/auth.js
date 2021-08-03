import axios from "axios"

export const auth = {
  namespaced: true,
  state: {
    token:
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2RmQGFzZGYuYXNkZiIsImV4cCI6MTYyODc1NjM5Mn0.7qOf3aR0qTCOVqt7M6mk9F2TN_wuP335fsO63K0ay-I",
  },
  getters: {},
  mutations: {},
  actions: {
    resetPassword(context, email) {
      console.log(email)
      return axios({
        method: "put",
        url: "/api/v1/users/reset-password",
        data: email,
      })
    },
  },
}
