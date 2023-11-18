import React,{useEffect,useState,forwardRef,useImperativeHandle} from "react";
import { useNavigate } from "react-router-dom";
import dayjs from 'dayjs';
import { Alert, Calendar,FloatButton,Button,Modal,Drawer,Form,message, InputNumber,Input  } from 'antd';
import {  ArrowLeftOutlined  } from '@ant-design/icons';
const Reservation = (props,ref) => {
    const navigate = useNavigate();
    const [value, setValue] = useState(() => dayjs());
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedValue, setSelectedValue] = useState(() => dayjs());
    const onSelect = (newValue) => {
      setValue(newValue);
      setSelectedValue(newValue);
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
    // console.log(props,55,navigate,value)
    useEffect(()=>{
        return ()=>{
         
        }
    },[])
    function timeSlot (step) {   //  step = 间隔的分钟
      var date = new Date()
      const a='00'*1
       date.setHours(a)    // 时分秒设置从零点开始
       date.setSeconds(a)
      date.setUTCMinutes(a)
     
      var arr = [], timeArr = [];
      var slotNum = 24*60/step   // 算出多少个间隔
      for (var f = 0; f < slotNum; f++) {   //  stepM * f = 24H*60M
          // arr.push(new Date(Number(date.getTime()) + Number(step*60*1000*f)))   //  标准时间数组
          var time = new Date(Number(date.getTime()) + Number(step*60*1000*f))  // 获取：零点的时间 + 每次递增的时间
          var hour = '', sec = '';
          time.getHours() < 10 ? hour = '0' + time.getHours() : hour = time.getHours()  // 获取小时
          time.getMinutes() < 10 ? sec = '0' + time.getMinutes() : sec = time.getMinutes() 
          timeArr.push(hour + ':' + sec)
      }
        const mapArr=timeArr.map((item,index)=>{
          const _item={
            key:index,
            value:item,
            // ...
         }
          if(item.split(':')[0]<12){
            _item.value=item+'am'
            // _item.type='已预定'
          }else{
            _item.value=item+'pm'
          }
          return _item
        })
      return mapArr
  }
  const [TimeValue, setTimeValue] = useState(timeSlot(30))
  const showModal = () => {
    setIsModalOpen(true);
   
  };
   //getData ...todo
  const getData=()=>{

  }
  const handleOk = () => {
    
    form.validateFields({ validateOnly: true }).then(
      () => {
        //savaData
        //...todo
        setIsModalOpen(false);
        form.resetFields()
        // console.log(form.getFieldsValue())
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

  const showDrawer = () => {
    setOpen(true);
    getData()
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
      // // overflowY: 'auto',
      // padding:'20px',
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
        TimeValue.map((item,index)=>{
          return (<div c className={item.type? 'disabled item':'item'} onClick={ showModal} key={item.value}>

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
