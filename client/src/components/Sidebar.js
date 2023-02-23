import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { BREAK_POINT_TABLET } from '../constants/index';
const Items = ['Outer', 'Top', 'Bottom', 'Onepiece', 'Hat', 'Shoes'];
const links = ['outer', 'top', 'bottom', 'onepiece', 'hat', 'shoes'];
const Sidebar = () => {
  const usenavigate = useNavigate();
  return (
    <SWrapper>
      <SidebarBox>
        <div>
          <p className="title">Category</p>
        </div>
        <ItemList>
          <div>
            {Items.map((item, index) => (
              <SList
                key={index}
                onClick={() => usenavigate(`/category/${links[index]}`)}
              >
                {item}
              </SList>
            ))}
          </div>
        </ItemList>
      </SidebarBox>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  position: fixed;
  display: flex;
  width: 20%;
  justify-content: flex-end;
  min-width: 110px;
  /* 768px이하일때 사이드바 없애기 */
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    display: none;
    visibility: hidden;
  }
`;

//전체부분
const SidebarBox = styled.div`
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 30px 20px 0px;
    font-family: 'Gothic A1', sans-serif;
  }
`;
const ItemList = styled.div`
  border-right: 2px solid gray;
`;
const SList = styled.button`
  font-size: 15px;
  display: flex;
  border: none;
  cursor: pointer;
  padding: 10px 3px;
  background-color: #ffff;
  :focus {
    color: #196ba5;
    background-color: #faf6e9;
    width: 108.062px;
  }
  :hover {
    color: #196ba5;
    background-color: #faf6e9;
    width: 108.062px;
  }
`;

export default Sidebar;
