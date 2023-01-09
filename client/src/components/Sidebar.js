// import React from 'react';
import styled from 'styled-components';
const Sidebar = () => {
  return (
    <SWrapper>
      <SidebarBox>
        <div>
          <p className="font_size_mid">카테고리</p>
        </div>
        <ItemList>
          <div>
            <p className="font_size_small"> 아우터</p>
            <p className="font_size_small"> 상의</p>
            <p className="font_size_small"> 하의</p>
            <p className="font_size_small"> 원피스</p>
            <p className="font_size_small"> 모자</p>
            <p className="font_size_small"> 신발</p>
          </div>
        </ItemList>
      </SidebarBox>
    </SWrapper>
  );
};

//전체부분
const SWrapper = styled.div`
  /* position: fixed;
  top: 0;
  overflow: hidden; */

  width: 160px;
  height: 100vh;
  display: flex;
  justify-content: center;
  /* text-align: center; */
  background-color: #faf4c5;
  margin-right: auto;
`;

const SidebarBox = styled.div`
  .font_size_mid {
    font-size: 18px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 0px 20px 0px;
  }
`;
const ItemList = styled.div`
  .font_size_small {
    font-size: 14px;
    cursor: pointer;
    padding: 10px 0px;
    :hover {
      color: #bb2649;
    }
  }
`;
export default Sidebar;
