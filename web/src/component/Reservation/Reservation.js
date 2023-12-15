
import React,{useEffect,useMemo,useState,forwardRef,useImperativeHandle} from "react";
import { useNavigate } from "react-router-dom";
import dayjs from 'dayjs';
import { Alert, Calendar,FloatButton,Button,Modal,Drawer,Form,message, InputNumber,Input,Spin  } from 'antd';
import {  ArrowLeftOutlined  } from '@ant-design/icons';
import axios from "axios";
import { debounce } from 'lodash';
const Reservation = (props,ref) => {
  const navigate = useNavigate();
  const [value, setValue] = useState(() => dayjs());
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [item1, setItem] = useState({});
  const [TimeValue, setTimeValue] = useState(timeSlot(30));
  const [List, setList] = useState({});
  const [data, setData] = useState([]);
  const [selectedValue, setSelectedValue] = useState(() => dayjs());
  const onSelect = (newValue) => {
    setValue(newValue);
    setSelectedValue(newValue);
    getData(dayjs(newValue).format('YYYY-MM-DD'))
  };
  const onPanelChange = (newValue) => {
    setValue(newValue);
  };
  const disabledDate = (time) => {
    return time.$d.getTime() < Date.now()-86400000 ;
  };
  const cellRender = (current, info) => {
    if(current.$d.getTime() < Date.now()-86400000 ){
      //  return <Alert message="Time has passed" type="warning" />;
    }
  };
  const initDay=()=>{
    setValue(dayjs())
  }
  useEffect(()=>{
    change()

    return ()=>{

    }
  },[List,item1])

  const change=()=>{
    const data=[...timeSlot(30)]

    for(let i=0;i<List?.length;i++){
      const item = data.findIndex(item=>item.value===List[i]?.value)
      if(item>=0){
        data[item].type='Booked'
      }

    }

    setData(data)

  }
  function timeSlot (step) {   //  step = 间隔的分�?
    var date = new Date()
    const a='00'*1
    date.setHours(a)    // 时分秒设置从零点�?�?
    date.setSeconds(a)
    date.setUTCMinutes(a)

    var arr = [], timeArr = [];
    var slotNum = 24*60/step   // 算出多少个间�?
    for (var f = 0; f < slotNum; f++) {   //  stepM * f = 24H*60M
      // arr.push(new Date(Number(date.getTime()) + Number(step*60*1000*f)))   //  标准时间数组
      var time = new Date(Number(date.getTime()) + Number(step*60*1000*f))  // 获取：零点的时间 + 每次递增的时�?
      var hour = '', sec = '';
      time.getHours() < 10 ? hour = '0' + time.getHours() : hour = time.getHours()  // 获取小时
      time.getMinutes() < 10 ? sec = '0' + time.getMinutes() : sec = time.getMinutes()
      timeArr.push(hour + ':' + sec)
    }
    const mapArr=timeArr.map((item,index)=>{
      const _item={
        key:index,
        value:item,
        id:Math.random()
        // ...
      }
      if(item.split(':')[0]<12){
        _item.value=item+'am'
        // _item.type='已预�?'
      }else{
        _item.value=item+'pm'
      }
      return _item
    })
    return mapArr
  }

  const showModal = (item,index) => {

    setIsModalOpen(true);
    setItem(item)

  };
  //getData ...todo
  const getData= async(day)=>{
    const obj={
      id:'ab99909977',
      // 到时值换为props.id
    }
    setLoading(true)
    const {status,data}= await axios.post('/api/findById/'+obj.id+`?i=${new Date().getTime()}`)
    if(status===200){

      const listData=data?.order??[]

      const item=listData.find(item=>item.day===day)??null
      setList(item?.orders??[])
    }
    setLoading(false)

  }
  const handleOk = () => {

    form.validateFields({ validateOnly: true }).then(
      async () => {

        const v=item1.value
        const f=form.getFieldsValue()?.PeopleNumber
        const da={
          // id:props?.id,
          id:'ab99909977',
          day:dayjs(value).format('YYYY-MM-DD'),
          itemDto:{
            time:dayjs().format('YYYY-MM-DD HH:mm:ss'),
            value:v,
            peopleNumber:String(f),

          }
        }

        axios.post('/api/updateMg',da , {
          header: {
            'Content-type': 'application/json'
          }}).then(async(res)=>{
          if(res.data==='抱歉，你预定晚了'){
            message.warning('Sorry, you made a late reservation')
          }else{
            message.success('Appointment successful')
          }

          item1.type='Booked'
          const _data=[...data].map(i=>{
            if(i.key===item1.key){
              i.type=item1.type
            }
            return i
          })
          setData(_data)
          getData(dayjs(value).format('YYYY-MM-DD'))
        })
        setIsModalOpen(false);
        form.resetFields()
      },
      () => {
        message.error('Submit failed!');
      })
  };
  const handleCancel = () => {
    setIsModalOpen(false);
    form.resetFields()
  };
  const [open, setOpen] = useState(false);

  const showDrawer =async () => {
    setOpen(true);

    await getData(dayjs(value).format('YYYY-MM-DD'))
    //  change()
  };

  const onClose = () => {
    setOpen(false);
  };
  const onFinish = (values) => {
    console.log('Success:', values);
  };
  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };
  // console.log(TimeValue)
  useImperativeHandle(ref,()=>({
    showDrawer
  }))
  const [form] = Form.useForm();
  return (
    <div style={{
      height: props.windowHeight,

    }
    }
         ref={ref}
         className="calendar"
    >
      <Button type="primary" style={{marginTop:'20px'}} onClick={showDrawer}>Reservation</Button>

      <Drawer mask={false} title="Reservation" placement="right" onClose={onClose} open={open} width='40%' style={{paddingBottom:'50px'}}>

        <Alert message={`You selected date: ${selectedValue?.format('YYYY-MM-DD')}`} />
        <FloatButton icon={< ArrowLeftOutlined  />}      tooltip={<div>Select the current time and date</div>} onClick={initDay}/>
        <Calendar value={value} onSelect={onSelect} onPanelChange={onPanelChange}   disabledDate={disabledDate} cellRender={cellRender}
                  fullscreen={false}
                  className="calendar"
        />
        <div style={{height:200,
          marginTop:40}} className="flex">
          {
            data.map((item,index)=>{
              return (<div c className={item.type? 'disabled item':'item'} onClick={ ()=>showModal(item,index)} key={item.value}>

                <p style={{fontSize:'16px'}}>  {item.value}</p>
                <h3>{item.type??''}</h3>
              </div>)
            })

          }
        </div>

        <Modal mask={false} title="Reservation" open={isModalOpen} onOk={handleOk} onCancel={handleCancel} >
          <Form
            form={form}
            name="basic"
            labelCol={{
              span: 8,
            }}
            wrapperCol={{
              span: 16,
            }}
            style={{
              maxWidth: 600,
            }}
            initialValues={{
              remember: true,
            }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
          >
            <Form.Item
              label="PeopleNumber"
              name="PeopleNumber"
              rules={[
                {
                  required: true,
                  message: 'Please input your PeopleNumber!',
                },
              ]}
            >
              <InputNumber min={1} max={6}  />
            </Form.Item>
          </Form>
        </Modal>
      </Drawer>
    </div>
  )
}
export default forwardRef(Reservation)
