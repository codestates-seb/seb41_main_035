// import React from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
const Sidebar = () => {
  const usenavigate = useNavigate();
  return (
    <SWrapper>
      <SidebarBox>
        <div>
          <p className="title">카테고리</p>
        </div>
        <ItemList>
          <div>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/outer`);
              }}
            >
              {' '}
              아우터
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/top`);
              }}
            >
              {' '}
              상의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/bottom`);
              }}
            >
              {' '}
              하의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/onepiece`);
              }}
            >
              {' '}
              원피스
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/hat`);
              }}
            >
              {' '}
              모자
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/shoes`);
              }}
            >
              {' '}
              신발
            </p>
          </div>
        </ItemList>
      </SidebarBox>
    </SWrapper>
  );
};

//전체부분
const SWrapper = styled.div`
  /* position: fixed;
  top: 0;*/
  position: sticky;
  display: flex;
  justify-content: center;
  /* text-align: center; */
`;

const SidebarBox = styled.div`
  width: 9vw;
  height: 70vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #faf4c5;
  margin-right: auto;
  .title {
    font-size: 17px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 0px 20px 20px;
  }
`;
const ItemList = styled.div`
  .items {
    font-size: 14px;
    cursor: pointer;
    padding: 10px 0px;
    :hover {
      color: #bb2649;
    }
  }
`;
export default Sidebar;
