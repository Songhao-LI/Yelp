/*
 * @Author: Ren Bing
 * @Date: 2023-11-14 15:18:01
 * @LastEditors: Ren Bing
 * @LastEditTime: 2023-11-14 16:12:08
 * @Description: 请填写简介
 */
import React, {useState} from 'react'
import { Layout } from "antd"
import { BrowserRouter, Routes, Route } from "react-router-dom"

// 组件
import Head from './component/Header/Head'
import Body from "./component/Body"
import Foot from "./component/Foot"
import Detail from "./component/Detail/Detail";
import Reservation from "./component/Reservation/Reservation"
const App = () => {
  const [bodyHeight, setBodyHeight] = useState(window.innerHeight - 64 - 64)
  const [newEvent, setNewEvent] = useState(0)

  const newEventListener = (version) => {
    setNewEvent(version)
  }

  return (
    <BrowserRouter>
      <Layout>
        <Head newEventCallback={newEventListener}></Head>
        <Routes>
          <Route path='/' element={<Body windowHeight={bodyHeight} newEventNotice={newEvent}></Body>}></Route>
          <Route path='/detail' element={<Detail windowHeight={bodyHeight}></Detail>}></Route>
        </Routes>
        <Foot></Foot>
      </Layout>
    </BrowserRouter>
  )
}
export default App